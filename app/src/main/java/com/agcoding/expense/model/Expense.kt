package com.agcoding.expense.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID


@Entity(tableName = "expense_tbl")
data class Expense(
    @PrimaryKey
    val id  : UUID = UUID.randomUUID(),

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "amount")
    val amount: Double,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "note")
    val notes: String = "",

    @ColumnInfo(name = "timestamp")
    val timestamp: LocalDateTime = LocalDateTime.now()
)