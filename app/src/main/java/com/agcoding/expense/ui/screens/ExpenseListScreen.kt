package com.agcoding.expense.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.agcoding.expense.R
import com.agcoding.expense.components.CategoryHeader
import com.agcoding.expense.components.DatePickerScreen
import com.agcoding.expense.components.ExpenseItem
import com.agcoding.expense.components.TimeHeader
import com.agcoding.expense.navigation.ExpenseScreens
import com.agcoding.expense.viewmodels.ExpenseViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    viewModel: ExpenseViewModel
) {
    listOf(
        ExpenseScreens.ExpenseList,
        ExpenseScreens.ExpenseReport
    )
    val selectedDate by viewModel.selectedDate
    val groupByCategory by viewModel.groupByCategory
    val totalSpentToday = viewModel.getTotalSpentToday()
    val dateText = if (selectedDate == LocalDate.now()) {
        stringResource(R.string.total_spent_today)
    } else {
        "Total Spent on ${selectedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))}"
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Header with total and date selector
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = dateText,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "₹${String.format(Locale.US, "%.2f", totalSpentToday)}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Date selector and grouping options
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DatePickerScreen(viewModel)

                // Grouping toggle
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (groupByCategory) "By Category" else "By Time",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = groupByCategory,
                        onCheckedChange = {
                            viewModel.groupByCategory.value = it
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Expenses list
            val expensesForDate = viewModel.getExpensesForDate(selectedDate)

            if (expensesForDate.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No expenses for ${
                                selectedDate.format(
                                    DateTimeFormatter.ofPattern(
                                        "MMM dd"
                                    )
                                )
                            }",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (groupByCategory) {
                        val groupedExpenses = viewModel.getExpensesGroupedByCategory(selectedDate)
                        groupedExpenses.forEach { (category, expenses) ->
                            item {
                                CategoryHeader(category, expenses.sumOf { it.amount })
                            }
                            items(expenses) { expense ->
                                ExpenseItem(
                                    expense = expense,
                                    onDelete = {
                                        viewModel.deleteExpense(expense)
                                    }
                                )
                            }
                        }
                    } else {
                        val groupedExpenses = viewModel.getExpensesGroupedByTime(selectedDate)
                        groupedExpenses.forEach { (time, expenses) ->
                            item {
                                TimeHeader(time, expenses.sumOf { it.amount })
                            }
                            items(expenses) { expense ->
                                ExpenseItem(
                                    expense = expense,
                                    onDelete = { viewModel.deleteExpense(expense) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}