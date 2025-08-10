package com.agcoding.expense.viewmodels

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agcoding.expense.model.Expense
import com.agcoding.expense.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import androidx.compose.runtime.State


@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _isDarkTheme = mutableStateOf(false)
    val isDarkTheme: State<Boolean> get() = _isDarkTheme

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }
    private val _expenses = mutableStateListOf<Expense>()
    val expenses: List<Expense> get() = _expenses

    val totalSpentToday = mutableDoubleStateOf(0.0)
    val selectedDate = mutableStateOf(LocalDate.now())
    val groupByCategory = mutableStateOf(false)

    val categories = listOf("Staff", "Travel", "Food", "Utility")

    init {
        viewModelScope.launch {
            repository.getAllExpenses().collect { list ->
                _expenses.clear()
                _expenses.addAll(list)
                updateTotalSpentToday()
            }
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repository.insertExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }

    }



    private fun updateTotalSpentToday() {
        val today = LocalDate.now()
        totalSpentToday.doubleValue = expenses
            .filter { it.timestamp.toLocalDate() == today }
            .sumOf { it.amount }
    }

    fun getTotalSpentToday(): Double {
        return totalSpentToday.doubleValue
    }



    fun getExpensesForDate(date: LocalDate): List<Expense> {
        return expenses.filter { it.timestamp.toLocalDate() == date }
    }

    fun getExpensesForLast7Days(): List<Expense> {
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(6)
        return expenses.filter {
            val expenseDate = it.timestamp.toLocalDate()
            !expenseDate.isBefore(startDate) && !expenseDate.isAfter(endDate)
        }
    }

    fun getDailyTotals(): Map<LocalDate, Double> {
        val last7Days = getExpensesForLast7Days()
        return last7Days.groupBy { it.timestamp.toLocalDate() }
            .mapValues { (_, expenses) -> expenses.sumOf { it.amount } }
    }

    fun getCategoryTotals(): Map<String, Double> {
        val last7Days = getExpensesForLast7Days()
        return last7Days.groupBy { it.category }
            .mapValues { (_, expenses) -> expenses.sumOf { it.amount } }
    }

    fun getTotalForLast7Days(): Double {
        return getExpensesForLast7Days().sumOf { it.amount }
    }

    fun getAverageDailySpending(): Double {
        val dailyTotals = getDailyTotals()
        return if (dailyTotals.isNotEmpty()) dailyTotals.values.average() else 0.0
    }

    fun getExpensesGroupedByCategory(date: LocalDate): Map<String, List<Expense>> {
        return getExpensesForDate(date).groupBy { it.category }
    }

    fun getExpensesGroupedByTime(date: LocalDate): Map<String, List<Expense>> {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return getExpensesForDate(date).groupBy {
            it.timestamp.format(formatter)
        }
    }
    fun onDateSelected(date: LocalDate) {
        selectedDate.value = date

        // Optionally, you can recalculate daily total immediately after date change
        totalSpentToday.doubleValue = expenses
            .filter { it.timestamp.toLocalDate() == date }
            .sumOf { it.amount }
    }
}