package com.example.task_management_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    val allTasks: LiveData<List<Task>> = repository.allTasks.asLiveData()
    fun insert(TASK_NAME: String? ,DESCRIPTION: String?, PRIORITY: Int?, DEADLINE: String?) = viewModelScope.launch {
        if (TASK_NAME != null) {
            repository.insert(TASK_NAME, DESCRIPTION, PRIORITY, DEADLINE)
        }
    }
}

class WordViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
