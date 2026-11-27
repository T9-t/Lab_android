package com.example.tasks.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val date: String,
    val time: String,
    val description: String
)

enum class SortOption(val label: String) {
    DATE_ASC("По времени (возрастание)"),
    DATE_DESC("По времени (убывание)"),
    ALPHABETICAL("По алфавиту")
}