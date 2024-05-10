package com.example.task_management_app

import android.content.Context
import android.widget.Toast
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: Flow<List<Task>> = taskDao.getAlphabetizedTasks()

    @WorkerThread
    suspend fun getTaskByName(taskName: String): Task? {
        return taskDao.getTaskById(taskName)
    }

    @WorkerThread
    suspend fun insert(context: Context, TASK_NAME: String? ,DESCRIPTION: String?, PRIORITY: Int?, DEADLINE: String?) {
        try {
            val task = Task(
                TASK_NAME ?: "",
                DESCRIPTION ?: "",
                PRIORITY ?: 0,
                DEADLINE ?: ""
            )
            val chk = TASK_NAME?.let { getTaskByName(it) }
            println("chk $chk")
            if (chk != null) {
                Toast.makeText(context, "Task already exists!", Toast.LENGTH_SHORT).show()
            } else {
                taskDao.insert(task)
                Toast.makeText(context, "Task inserted successfully!", Toast.LENGTH_SHORT).show()
            }


        } catch (e: Exception) {
            // Handle exceptions, e.g., if the task already exists
            Toast.makeText(context, "Failed to insert task: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    @WorkerThread
    suspend fun delete(taskName: String) {
        taskDao.deleteTaskByName(taskName)
    }

    @WorkerThread
    suspend fun deleteTaskByName(taskName: String) {
        taskDao.deleteTaskByName(taskName)
    }

    @WorkerThread
    suspend fun update(context: Context, taskName: String, description: String, priority: Int, deadline: String) {
        taskDao.update(taskName, description, priority, deadline)
        Toast.makeText(context, "Task updated successfully!", Toast.LENGTH_SHORT).show()
    }
}

