package com.example.task_management_app

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey
    @ColumnInfo(name = "task")
    val task: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "priority")
    val priority: Int,
    @ColumnInfo(name = "deadline")
    val deadline: String
)
