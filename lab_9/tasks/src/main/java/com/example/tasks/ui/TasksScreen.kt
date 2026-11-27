package com.example.tasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasks.data.SortOption
import com.example.tasks.data.Task
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.example.tasks.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksAppBar(
    currentSortOption: SortOption,
    onSortOptionSelected: (SortOption) -> Unit,
    onCalendarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showSortMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(stringResource(R.string.app_name), fontWeight = FontWeight.Bold) },
        modifier = modifier,
        actions = {
            IconButton(onClick = onCalendarClick) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = stringResource(R.string.calendar_desc)
                )
            }
            Box {
                IconButton(onClick = { showSortMenu = true }) {
                    Icon(
                        Icons.Default.List,
                        contentDescription = stringResource(R.string.sort_desc)
                    )
                }
                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false }
                ) {
                    SortOption.entries.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option.label,
                                    fontWeight = if (option == currentSortOption) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            onClick = {
                                onSortOptionSelected(option)
                                showSortMenu = false
                            }
                        )
                    }
                }
            }
        }
    )
}

private sealed interface ActiveDialog {
    data class Details(val task: Task) : ActiveDialog
    data class Editor(val task: Task? = null) : ActiveDialog
    data object Calendar : ActiveDialog
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksApp(
    appViewModel: AppViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by appViewModel.uiState.collectAsState()
    val today = remember { LocalDate.now() }
    var activeDialog by remember { mutableStateOf<ActiveDialog?>(null) }

    Scaffold(
        topBar = {
            TasksAppBar(
                currentSortOption = uiState.sortOption,
                onSortOptionSelected = { appViewModel.selectSortOption(it) },
                onCalendarClick = { activeDialog = ActiveDialog.Calendar }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { activeDialog = ActiveDialog.Editor(task = null) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.create_task_desc),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val dates = remember(today) { (0L..60L).map { today.plusDays(it) } }
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(dates) { date ->
                    val isSelected = date == uiState.selectedDate
                    val bgColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(bgColor)
                            .clickable { appViewModel.selectDate(date) }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = if (date == today) stringResource(R.string.today_short)
                            else date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                            fontSize = 12.sp,
                            color = textColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = date.dayOfMonth.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                }
            }
            val dateTitle = if (uiState.selectedDate == today) stringResource(R.string.today)
            else uiState.selectedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(dateTitle, fontWeight = FontWeight.SemiBold, color = Color.Gray)
                Text(
                    stringResource(R.string.tasks_count, uiState.taskList.size),
                    color = Color.Gray
                )
            }
            if (uiState.taskList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.no_tasks_text), color = Color.Gray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.taskList, key = { it.id }) { task ->
                        TaskCard(
                            task = task,
                            onClick = { activeDialog = ActiveDialog.Details(task) }
                        )
                    }
                }
            }
        }
    }
    when (val dialog = activeDialog) {
        is ActiveDialog.Editor -> {
            CreationScreen(
                initialDate = uiState.selectedDate,
                taskToEdit = dialog.task,
                onSave = { task -> appViewModel.saveTask(task) },
                onDismiss = { activeDialog = null }
            )
        }
        is ActiveDialog.Calendar -> {
            SystemDatePickerDialog(
                initialDate = uiState.selectedDate,
                onDateSelected = { pickedDate -> appViewModel.selectDate(pickedDate) },
                onDismiss = { activeDialog = null }
            )
        }
        is ActiveDialog.Details -> {
            TaskDetailsDialog(
                task = dialog.task,
                onDelete = {
                    appViewModel.deleteTask(dialog.task)
                    activeDialog = null
                },
                onEdit = {
                    activeDialog = ActiveDialog.Editor(task = dialog.task)
                },
                onDismiss = { activeDialog = null }
            )
        }
        null -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreationScreen(
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit,
    initialDate: LocalDate = LocalDate.now(),
    taskToEdit: Task? = null
) {
    val context = LocalContext.current

    var showDatePicker by remember { mutableStateOf(false) }
    var title by remember(taskToEdit) { mutableStateOf(taskToEdit?.title ?: "") }
    var date by remember(taskToEdit, initialDate) { mutableStateOf(taskToEdit?.date ?: initialDate.toString()) }
    var time by remember(taskToEdit) { mutableStateOf(taskToEdit?.time ?: "12:00") }
    var description by remember(taskToEdit) { mutableStateOf(taskToEdit?.description ?: "") }
    val isEntryValid = title.isNotBlank()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.title_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { showDatePicker = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.date_button_label, date))
                    }
                    OutlinedButton(
                        onClick = {
                            val parts = time.split(":")
                            val hour = parts.getOrNull(0)?.toIntOrNull() ?: 12
                            val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0
                            TimePickerDialog(
                                context,
                                { _, h, m ->
                                    time = String.format(Locale.US, "%02d:%02d", h, m)
                                },
                                hour, minute, true
                            ).show()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.time_button_label, time))
                    }
                }
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.description_label)) },
                    minLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        onSave(
                            Task(
                                id = taskToEdit?.id ?: 0,
                                title = title.trim(),
                                date = date,
                                time = time,
                                description = description.trim()
                            )
                        )
                        onDismiss()
                    },
                    enabled = isEntryValid,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.save_action))
                }
            }
            if (showDatePicker) {
                val current = runCatching { LocalDate.parse(date) }.getOrDefault(LocalDate.now())
                SystemDatePickerDialog(
                    initialDate = current,
                    onDateSelected = { pickedDate ->
                        date = pickedDate.toString()
                    },
                    onDismiss = { showDatePicker = false }
                )
            }
        }
    }
}

@Composable
fun TaskDetailsDialog(
    task: Task,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(task.title, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text(
                    text = stringResource(R.string.task_date_time_info, task.date, task.time),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(task.description.ifBlank { stringResource(R.string.no_description) })
            }
        },
        confirmButton = {
            Row {
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_desc),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit_desc)
                    )
                }
            }
        }
    )
}

@Composable
fun SystemDatePickerDialog(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    DisposableEffect(initialDate) {
        val dialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
            },
            initialDate.year,
            initialDate.monthValue - 1,
            initialDate.dayOfMonth
        ).apply {
            setOnDismissListener { onDismiss() }
            show()
        }
        onDispose {
            dialog.dismiss()
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = task.time,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    task.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        task.description,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}