package com.example.task_management_app

import android.content.Context
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class TaskRepository(private val taskDao: TaskDao) {

    private val calendar: Calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH) + 1 // Month starts from 0
    private val day = calendar.get(Calendar.DAY_OF_MONTH)

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
                DEADLINE ?: "$year-$month-$day"
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
    suspend fun delete(task: Task) {
        taskDao.delete(task)
    }

    @WorkerThread
    suspend fun deleteTaskByName(taskName: String) {
        taskDao.deleteTaskByName(taskName)
    }

    @WorkerThread
    suspend fun update(task: Task) {
        taskDao.update(task)
    }
}

