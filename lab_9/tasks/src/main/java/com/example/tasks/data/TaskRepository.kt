package com.example.tasks.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TasksRepository {
    fun getTasksByDateStream(date: LocalDate): Flow<List<Task>>
    fun getTaskStream(id: Int): Flow<Task?>
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)

    val selectedDateStream: Flow<LocalDate>
    val sortOptionStream: Flow<SortOption>
    suspend fun saveSelectedDate(date: LocalDate)
    suspend fun saveSortOption(option: SortOption)
}