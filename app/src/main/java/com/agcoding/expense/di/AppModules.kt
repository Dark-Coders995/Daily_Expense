package com.agcoding.expense.di

import android.content.Context
import androidx.room.Room
import com.agcoding.expense.data.ExpenseDatabase
import com.agcoding.expense.data.ExpenseDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModules {

    @Singleton
    @Provides
    fun providesExpenseDao(
        expenseDatabase : ExpenseDatabase) : ExpenseDatabaseDao{
        return expenseDatabase.expenseDao()
    }

    @Singleton
    @Provides
    fun providesAppDatabase(@ApplicationContext context: Context): ExpenseDatabase{
        return Room.databaseBuilder(
            context = context,
            ExpenseDatabase :: class.java,
            "expense_db"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }
}