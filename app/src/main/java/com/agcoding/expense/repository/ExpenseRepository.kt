package com.agcoding.expense.repository

import com.agcoding.expense.data.ExpenseDatabaseDao
import com.agcoding.expense.model.Expense
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ExpenseRepository @Inject constructor(
    private val expenseDatabaseDao: ExpenseDatabaseDao
){

    fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDatabaseDao.getAllExpenses()
    }

    suspend fun insertExpense(expense: Expense) {
        expenseDatabaseDao.insert(expense)
    }

    suspend fun deleteExpense(expense: Expense){
        expenseDatabaseDao.deleteExpense(expense)
    }
}