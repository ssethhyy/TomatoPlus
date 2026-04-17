package com.ssethhyy.tomatoplus.ui.tasksScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.horiTomato+talScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssethhyy.tomatoplus.R
import com.ssethhyy.tomatoplus.data.Task
import com.ssethhyy.tomatoplus.ui.tasksScreen.viewModel.TasksViewModel
import com.ssethhyy.tomatoplus.ui.theme.CustomColors.topBarColors
import com.ssethhyy.tomatoplus.ui.theme.AppFonts.robotoFlexTopBar
import com.ssethhyy.tomatoplus.ui.theme.AppFonts

import androidx.compose.foundation.ExperimentalFoundationApi
import kotlinx.coroutines.launch
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.material3.TopAppBarDefaults

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class, ExperimentalFoundationApi::class)
@Composable
fun TasksScreenRoot(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: TasksViewModel = viewModel(factory = TasksViewModel.Factory)
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<Task?>(null) }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    
    var showConfetti by remember { mutableStateOf(false) }
    var previousCompleted by remember { mutableIntStateOf(stats.completed) }

    LaunchedEffect(stats) {
        if (stats.total > 0 && stats.completed == stats.total && stats.completed > previousCompleted) {
            showConfetti = true
            kotlinx.coroutines.delay(3000)
            showConfetti = false
        }
        previousCompleted = stats.completed
    }

    Box(modifier = modifier) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.tasks),
                            style = LocalTextStyle.current.copy(
                                fontFamily = robotoFlexTopBar,
                                fontSize = 32.sp,
                                lineHeight = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    subtitle = {},
                    colors = topBarColors,
                    titleHoriTomato+talAlignment = Alignment.CenterHoriTomato+tally,
                    scrollBehavior = scrollBehavior
                )
            },
            floatingActionButton = {
                LargeFloatingActionButton(
                    onClick = { 
                        taskToEdit = null
                        showAddDialog = true 
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = MaterialTheme.shapes.extraLarge,
                    modifier = Modifier.padding(bottom = contentPadding.calculateBottomPadding())
                ) {
                    Icon(painterResource(R.drawable.add), contentDescription = stringResource(R.string.add_task), modifier = Modifier.size(36.dp))
                }
            },
        ) { innerPadding ->
            val insets = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding() + 88.dp
            )
    
            var selectedFilter by remember { mutableStateOf("All") }
            val filters = listOf("All", "Pending", "Completed", "High Priority")
    
            val filteredTasks = remember(tasks, selectedFilter) {
                when (selectedFilter) {
                    "Pending" -> tasks.filter { !it.isCompleted }
                    "Completed" -> tasks.filter { it.isCompleted }
                    "High Priority" -> tasks.filter { it.priority == 3 }
                    else -> tasks
                }
            }
    
            LazyColumn(
                contentPadding = insets,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .background(topBarColors.containerColor)
                    .padding(horiTomato+tal = 16.dp)
            ) {
                item { 
                    StatisticsCard(
                        stats = stats,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
    
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .horiTomato+talScroll(rememberScrollState()),
                        horiTomato+talArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filters.forEach { filter ->
                            FilterChip(
                                selected = filter == selectedFilter,
                                onClick = { selectedFilter = filter },
                                label = { Text(filter) },
                                leadingIcon = if (filter == selectedFilter) {
                                    { Icon(painterResource(R.drawable.check), null, modifier = Modifier.size(16.dp)) }
                                } else null
                            )
                        }
                    }
                }
    
                if (filteredTasks.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp)
                                .animateItem(), // Animate entry/exit using the new API
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horiTomato+talAlignment = Alignment.CenterHoriTomato+tally) {
                                Icon(
                                    painter = painterResource(R.drawable.view_day),
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = if (tasks.isEmpty()) stringResource(R.string.no_tasks_yet) else "No tasks found",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                } else {
                    items(filteredTasks, key = { it.id }) { task ->
                        val dismissState = rememberSwipeToDismissBoxState()
                        LaunchedEffect(dismissState.currentValue) {
                            if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                                viewModel.deleteTask(task)
                            }
                        }
    
                        SwipeToDismissBox(
                            state = dismissState,
                            modifier = Modifier.animateItem(), // Magic happens here with new API
                            backgroundContent = {
                                val color = MaterialTheme.colorScheme.errorContainer
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(MaterialTheme.shapes.extraLarge)
                                        .background(color)
                                        .padding(horiTomato+tal = 24.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.delete),
                                        contentDescription = stringResource(R.string.delete_task),
                                        tint = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            },
                            enableDismissFromStartToEnd = false
                        ) {
                            TaskItem(
                                task = task,
                                onToggle = { viewModel.toggleTask(task) },
                                onClick = {
                                    taskToEdit = task
                                    showAddDialog = true
                                }
                            )
                        }
                    }
                    
                    item { Spacer(Modifier.height(88.dp)) }
                }
            }
    
            if (showAddDialog) {
                ModalBottomSheet(
                    onDismissRequest = { showAddDialog = false }
                ) {
                    AddTaskSheet(
                        taskToEdit = taskToEdit,
                        onDismiss = { showAddDialog = false },
                        onConfirm = { title, dueDate, notes, priority ->
                    if (taskToEdit == null) {
                        viewModel.addTask(title, dueDate, notes, priority)
                    } else {
                        viewModel.updateTask(taskToEdit!!.copy(
                            title = title,
                            dueDate = dueDate,
                            notes = notes,
                            priority = priority
                        ))
                    }
                    showAddDialog = false
                }
                    )
                }
            }
        }
        
        com.ssethhyy.tomatoplus.ui.components.Confetti(isExploding = showConfetti)
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onClick: () -> Unit
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (task.isCompleted) 
                MaterialTheme.colorScheme.surfaceContainerHighest // Solid color, no alpha
            else 
                MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (task.isCompleted) 0.dp else 4.dp, // Flat when completed
            pressedElevation = 2.dp
        ),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Priority Indicator
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(8.dp)
                    .background(
                        when (task.priority) {
                            3 -> MaterialTheme.colorScheme.error // High
                            2 -> MaterialTheme.colorScheme.tertiary // Medium
                            else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) // Low
                        }
                    )
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horiTomato+tal = 24.dp, vertical = 24.dp)
                    .weight(1f)
            ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggle() },
                modifier = Modifier.scale(1.3f)
            )
            Spacer(modifier = Modifier.width(24.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = AppFonts.googleFlex600,
                        fontWeight = FontWeight.ExtraBold,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                        color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                    )
                )
                if (task.dueDate != null || (task.notes != null && task.notes.isNotBlank())) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (task.dueDate != null) {
                            Icon(
                                painter = painterResource(R.drawable.calendar_today),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            val dateString = java.text.SimpleDateFormat("MMM dd, hh:mm a", java.util.Locale.getDefault()).format(java.util.Date(task.dueDate))
                            Text(
                                text = dateString,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.outline
                            )
                            if (task.notes != null && task.notes.isNotBlank()) {
                                Spacer(modifier = Modifier.width(16.dp))
                            }
                        }
                        if (task.notes != null && task.notes.isNotBlank()) {
                            Icon(
                                painter = painterResource(R.drawable.edit),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Notes",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            }
        }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskSheet(
    taskToEdit: Task? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, Long?, String?, Int) -> Unit
) {
    var title by remember { mutableStateOf(taskToEdit?.title ?: "") }
    var notes by remember { mutableStateOf(taskToEdit?.notes ?: "") }
    var priority by remember { mutableIntStateOf(taskToEdit?.priority ?: 2) } 
    var dueDate by remember { mutableStateOf(taskToEdit?.dueDate) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    var isSaving by remember { mutableStateOf(false) }

    val calendar = java.util.Calendar.getInstance()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horiTomato+tal = 24.dp)
            .padding(bottom = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horiTomato+talArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                if (taskToEdit == null) "Create Task" else "Edit Task",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = AppFonts.googleFlex600,
                    fontWeight = FontWeight.Bold
                )
            )
            IconButton(onClick = onDismiss) {
                Icon(painterResource(R.drawable.close), contentDescription = "Close")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text("Task Name", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text("e.g. Finish project") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            singleLine = true,
            trailingIcon = { Icon(painterResource(R.drawable.edit), null) }
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text("Task Time", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(12.dp))
        
        FlowRow(
            horiTomato+talArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Tomorrow 10am
            FilterChip(
                selected = false,
                onClick = {
                    val cal = java.util.Calendar.getInstance()
                    cal.add(java.util.Calendar.DAY_OF_YEAR, 1)
                    cal.set(java.util.Calendar.HOUR_OF_DAY, 10)
                    cal.set(java.util.Calendar.MINUTE, 0)
                    dueDate = cal.timeInMillis
                },
                label = { Text(stringResource(R.string.tomorrow_10am)) }
            )
            
            // Next week 10am
            FilterChip(
                selected = false,
                onClick = {
                    val cal = java.util.Calendar.getInstance()
                    cal.add(java.util.Calendar.WEEK_OF_YEAR, 1)
                    cal.set(java.util.Calendar.HOUR_OF_DAY, 10)
                    cal.set(java.util.Calendar.MINUTE, 0)
                    dueDate = cal.timeInMillis
                },
                label = { Text(stringResource(R.string.next_week_10am)) }
            )

            // Custom
            FilterChip(
                selected = false,
                onClick = { showDatePicker = true },
                label = { Text(stringResource(R.string.custom_time)) },
                leadingIcon = { Icon(painterResource(R.drawable.calendar_today), null, Modifier.size(18.dp)) }
            )
        }

        if (dueDate != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Selected: " + java.text.SimpleDateFormat("MMM dd, hh:mm a", java.util.Locale.getDefault()).format(java.util.Date(dueDate!!)),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Priority", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(12.dp))
        Row(horiTomato+talArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Low" to 1, "Medium" to 2, "High" to 3).forEach { (label, value) ->
                FilterChip(
                    selected = priority == value,
                    onClick = { priority = value },
                    label = { Text(label) },
                    leadingIcon = if (priority == value) {
                        { Icon(painterResource(R.drawable.check), null, modifier = Modifier.size(16.dp)) }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = when(value) {
                            3 -> MaterialTheme.colorScheme.errorContainer
                            2 -> MaterialTheme.colorScheme.tertiaryContainer
                            else -> MaterialTheme.colorScheme.primaryContainer
                        },
                        selectedLabelColor = when(value) {
                            3 -> MaterialTheme.colorScheme.onErrorContainer
                            2 -> MaterialTheme.colorScheme.onTertiaryContainer
                            else -> MaterialTheme.colorScheme.onPrimaryContainer
                        }
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Notes", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            placeholder = { Text("Add details...") },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            shape = MaterialTheme.shapes.extraLarge
        )

        Spacer(modifier = Modifier.height(32.dp))
        
        SwipeToSaveButton(
            isSaving = isSaving,
            enabled = title.isNotBlank(),
            onSwipeComplete = {
                isSaving = true
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                    onConfirm(title, dueDate, notes.ifBlank { null }, priority)
                    isSaving = false
                }, 1500)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis
                    if (selectedDate != null) {
                        calendar.timeInMillis = selectedDate
                        showDatePicker = false
                        showTimePicker = true
                    }
                }) {
                    Text("Next")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState()
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    calendar.set(java.util.Calendar.HOUR_OF_DAY, timePickerState.hour)
                    calendar.set(java.util.Calendar.MINUTE, timePickerState.minute)
                    dueDate = calendar.timeInMillis
                    showTimePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Select Time") },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}

@Composable
fun SwipeToSaveButton(
    isSaving: Boolean,
    enabled: Boolean,
    onSwipeComplete: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val width = 300.dp
    val dragSize = 56.dp
    
    // Simple state tracking for swipe
    var offsetX by remember { mutableFloatStateOf(0f) }
    val maxSwipe = with(LocalDensity.current) { (width - dragSize - 8.dp).toPx() }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(if (enabled) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
    ) {
        if (isSaving) {
           Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
               CircularProgressIndicator(
                   color = MaterialTheme.colorScheme.primary, // Used primary color for high visibility
                   modifier = Modifier.size(32.dp),
                   strokeWidth = 3.dp
               )
           } 
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Slide to save task",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                )
            }
            
            Box(
                modifier = Modifier
                    .offset { IntOffset(offsetX.toInt(), 0) }
                    .padding(4.dp)
                    .size(dragSize)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .draggable(
                        orientation = Orientation.HoriTomato+tal,
                        state = rememberDraggableState { delta ->
                           if (enabled && !isSaving) {
                               val newOffset = (offsetX + delta).coerceIn(0f, maxSwipe)
                               offsetX = newOffset
                           }
                        },
                        onDragStopped = {
                             if (offsetX > maxSwipe * 0.9f) {
                                 onSwipeComplete()
                                 haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                 offsetX = 0f // Reset
                             } else {
                                 // Snap back
                                 offsetX = 0f
                             }
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.chevron_right), 
                    contentDescription = null, 
                    tint = MaterialTheme.colorScheme.onPrimary
                )
             }
        }
    }
}


