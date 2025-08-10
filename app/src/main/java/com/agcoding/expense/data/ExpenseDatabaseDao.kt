package com.agcoding.expense.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.agcoding.expense.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense : Expense)

    @Query("SELECT * FROM expense_tbl ORDER BY timestamp DESC")
    fun getAllExpenses(): Flow<List<Expense>>


    @Delete
    suspend fun deleteExpense(expense: Expense)
}