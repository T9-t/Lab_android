package com.example.tasks.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class OfflineTasksRepository(
    private val taskDao: TaskDao,
    private val dataStore: DataStore<Preferences>
) : TasksRepository {

    private companion object {
        val LAST_SELECTED_DATE = stringPreferencesKey("last_selected_date")
        val SORT_OPTION = stringPreferencesKey("sort_option")
    }

    override fun getTasksByDateStream(date: LocalDate): Flow<List<Task>> =
        taskDao.getTasksByDate(date.toString())

    override fun getTaskStream(id: Int): Flow<Task?> =
        taskDao.getTask(id)

    override suspend fun insertTask(task: Task) = taskDao.insert(task)

    override suspend fun updateTask(task: Task) = taskDao.update(task)

    override suspend fun deleteTask(task: Task) = taskDao.delete(task)

    override val selectedDateStream: Flow<LocalDate> = dataStore.data.map { prefs ->
        val savedDate = prefs[LAST_SELECTED_DATE]
        if (savedDate != null) LocalDate.parse(savedDate) else LocalDate.now()
    }
    override val sortOptionStream: Flow<SortOption> = dataStore.data.map { prefs ->
        val savedSort = prefs[SORT_OPTION]
        if (savedSort != null) SortOption.valueOf(savedSort) else SortOption.DATE_ASC
    }
    override suspend fun saveSelectedDate(date: LocalDate) {
        dataStore.edit { settings ->
            settings[LAST_SELECTED_DATE] = date.toString()
        }
    }
    override suspend fun saveSortOption(option: SortOption) {
        dataStore.edit { settings ->
            settings[SORT_OPTION] = option.name
        }
    }
}