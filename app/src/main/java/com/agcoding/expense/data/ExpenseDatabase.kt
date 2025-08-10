package com.agcoding.expense.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.agcoding.expense.model.Expense
import com.agcoding.expense.utils.Converters


@Database(entities = [Expense ::class] , version = 1 , exportSchema = false)
@TypeConverters(Converters :: class)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun expenseDao() : ExpenseDatabaseDao
}