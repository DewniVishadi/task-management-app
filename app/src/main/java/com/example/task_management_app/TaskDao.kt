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
    @Query("SELECT * FROM task_table ORDER BY task ASC")
    fun getAlphabetizedTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    // Additional operation to delete a specific task by its task name
    @Query("DELETE FROM task_table WHERE task = :taskName")
    suspend fun deleteTaskByName(taskName: String)

    // Additional operation to update a task
    @Update
    suspend fun update(task: Task)
}

