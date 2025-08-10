package com.agcoding.expense.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

sealed class ExpenseScreens(val route: String, val title: String, val icon: @Composable () -> Unit) {
    @OptIn(ExperimentalMaterial3Api::class)
    object ExpenseList : ExpenseScreens(
        route = "expense_list",
        title = "Expenses",
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = "List Icon"
            )
        }
    )

    object ExpenseReport : ExpenseScreens(
        route = "expense_report",
        title = "Reports",
        icon = { Icon(
            imageVector = Icons.Filled.Home,
            contentDescription =" Report") }
    )

    object ExpenseEntry : ExpenseScreens(
        route = "expense_entry",
        title = "Add Expense",
        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) }
    )
}