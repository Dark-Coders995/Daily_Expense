package com.agcoding.expense.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.agcoding.expense.ui.screens.ExpenseEntryScreen
import com.agcoding.expense.ui.screens.ExpenseListScreen
import com.agcoding.expense.ui.screens.ExpenseReportScreen
import com.agcoding.expense.viewmodels.ExpenseViewModel


@Composable
fun ExpenseNavigation(
    sharedViewModel: ExpenseViewModel
){
    val items = listOf(
        ExpenseScreens.ExpenseList,
        ExpenseScreens.ExpenseReport
    )
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = screen.icon,
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(ExpenseScreens.ExpenseEntry.route)
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Expense")
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = ExpenseScreens.ExpenseList.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(
                ExpenseScreens.ExpenseList.route) {
                ExpenseListScreen(
                    viewModel = sharedViewModel
                )
            }

            composable(ExpenseScreens.ExpenseReport.route) {
                ExpenseReportScreen(
                    viewModel = sharedViewModel
                )
            }

            composable(ExpenseScreens.ExpenseEntry.route) {

                ExpenseEntryScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    viewModel = sharedViewModel
                )
            }
        }
    }
}