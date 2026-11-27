package com.example.tasks.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tasks.TasksApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            AppViewModel(tasksApplication().container.tasksRepository)
        }
    }
}

fun CreationExtras.tasksApplication(): TasksApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TasksApplication)