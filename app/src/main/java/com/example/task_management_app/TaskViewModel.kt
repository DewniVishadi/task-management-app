package com.example.task_management_app

import android.content.Context
import android.webkit.WebSettings.RenderPriority
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    val allTasks: LiveData<List<Task>> = repository.allTasks.asLiveData()

    fun getSearchedTasks(task: String): LiveData<List<Task>>  {
        return  repository.getSearchedTasks(task).asLiveData()
    }

    fun insert(applicationContext: Context, TASK_NAME: String?, DESCRIPTION: String?, PRIORITY: Int?, DEADLINE: String?) = viewModelScope.launch {
        if (TASK_NAME != null) {
            repository.insert(applicationContext,TASK_NAME, DESCRIPTION, PRIORITY, DEADLINE)
        }
    }
    fun delete(taskName: String) = viewModelScope.launch {
        repository.delete(taskName)
    }

    fun update(applicationContext: Context, taskName: String, description: String, priority: Int, deadline: String) = viewModelScope.launch {
        repository.update(applicationContext, taskName, description, priority, deadline)
    }
}

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
