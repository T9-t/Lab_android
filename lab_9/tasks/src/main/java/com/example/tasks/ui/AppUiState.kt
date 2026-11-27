package com.example.tasks.ui

import com.example.tasks.data.SortOption
import com.example.tasks.data.Task
import java.time.LocalDate

data class AppUiState(
    val taskList: List<Task> = emptyList(),
    val selectedDate: LocalDate = LocalDate.now(),
    val sortOption: SortOption = SortOption.DATE_ASC
)