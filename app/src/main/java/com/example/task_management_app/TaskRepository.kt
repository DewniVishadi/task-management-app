/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.task_management_app

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class TaskRepository(private val taskDao: TaskDao) {

    private val calendar: Calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1 // Month starts from 0
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val allTasks: Flow<List<Task>> = taskDao.getAlphabetizedTasks()

    @WorkerThread
    suspend fun insert(TASK_NAME: String? ,DESCRIPTION: String?, PRIORITY: Int?, DEADLINE: String?) {
        val task = Task(
            TASK_NAME ?: "",
            DESCRIPTION ?: "",
            PRIORITY ?: 0,
            DEADLINE ?: "$year-$month-$day"
        )
        taskDao.insert(task)
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

