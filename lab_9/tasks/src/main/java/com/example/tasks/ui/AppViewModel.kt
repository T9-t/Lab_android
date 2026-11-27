package com.example.tasks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.SortOption
import com.example.tasks.data.Task
import com.example.tasks.data.TasksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class AppViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    val uiState: StateFlow<AppUiState> = combine(
        tasksRepository.selectedDateStream,
        tasksRepository.sortOptionStream
    ) { date, sort ->
        Pair(date, sort)
    }.flatMapLatest { (date, sort) ->
        tasksRepository.getTasksByDateStream(date).map { list ->
            val sortedList = when (sort) {
                SortOption.DATE_ASC -> list.sortedWith(compareBy({ it.date }, { it.time }))
                SortOption.DATE_DESC -> list.sortedWith(
                    compareByDescending<Task> { it.date }.thenByDescending { it.time }
                )
                SortOption.ALPHABETICAL -> list.sortedBy { it.title.lowercase() }
            }
            AppUiState(
                taskList = sortedList,
                selectedDate = date,
                sortOption = sort
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AppUiState()
    )

    fun selectDate(date: LocalDate) {
        viewModelScope.launch { tasksRepository.saveSelectedDate(date) }
    }

    fun selectSortOption(option: SortOption) {
        viewModelScope.launch { tasksRepository.saveSortOption(option) }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch { tasksRepository.deleteTask(task) }
    }

    fun saveTask(task: Task) {
        viewModelScope.launch {
            if (task.id == 0) {
                tasksRepository.insertTask(task)
            } else {
                tasksRepository.updateTask(task)
            }
            tasksRepository.saveSelectedDate(LocalDate.parse(task.date))
        }
    }
}