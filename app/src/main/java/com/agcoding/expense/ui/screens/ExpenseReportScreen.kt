package com.agcoding.expense.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.agcoding.expense.components.CategoryBreakdownCard
import com.agcoding.expense.components.DailyBreakdownCard
import com.agcoding.expense.components.InsightsCard
import com.agcoding.expense.components.SummaryCard
import com.agcoding.expense.viewmodels.ExpenseViewModel
import java.util.Locale

@Composable
fun ExpenseReportScreen(
    viewModel: ExpenseViewModel
) {
    val totalForLast7Days = viewModel.getTotalForLast7Days()
    val averageDailySpending = viewModel.getAverageDailySpending()
    val dailyTotals = viewModel.getDailyTotals()
    val categoryTotals = viewModel.getCategoryTotals()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Text(
                text = "Expense Report",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Last 7 Days",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Summary Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SummaryCard(
                    title = "Total Spent",
                    value = "₹${String.format(Locale.US, "%.2f", totalForLast7Days)}",
                    icon = Icons.Default.Email,
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = "Daily Average",
                    value = "₹${String.format(Locale.US, "%.2f", averageDailySpending)}",
                    icon = Icons.Default.ThumbUp,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Export Button
        item {
            OutlinedButton(
                onClick = { /* Export functionality */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Done, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Export Report")
            }
        }

        // Daily Breakdown
        item {
            Text(
                text = "Daily Breakdown",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        items(dailyTotals.toList().sortedBy { it.first }) { (date, total) ->
            DailyBreakdownCard(date, total)
        }

        // Category Breakdown
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Category Breakdown",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        items(categoryTotals.toList().sortedByDescending { it.second }) { (category, total) ->
            CategoryBreakdownCard(category, total, totalForLast7Days)
        }

        // Insights
        item {
            Spacer(modifier = Modifier.height(16.dp))
            InsightsCard(viewModel)
        }
    }
}
