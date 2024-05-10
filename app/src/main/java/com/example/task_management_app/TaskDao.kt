package com.example.task_management_app

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task_table ORDER BY priority DESC, deadline ASC ")
    fun getAlphabetizedTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE task = :task")
    suspend fun getTaskById(task: String): Task?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    @Query("DELETE FROM task_table WHERE task = :taskName")
    suspend fun deleteTaskByName(taskName: String)

    @Query("UPDATE task_table SET description = :description, priority = :priority, deadline = :deadline WHERE task = :taskName")
    suspend fun update(taskName: String, description: String, priority: Int, deadline: String)
}

