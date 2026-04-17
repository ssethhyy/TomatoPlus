package com.ssethhyy.tomatoplus.ui.tasksScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.ssethhyy.tomatoplus.TomatoPlusApplication
import com.ssethhyy.tomatoplus.data.Task
import com.ssethhyy.tomatoplus.data.TaskRepository

data class TaskStats(
    val total: Int,
    val completed: Int
)

class TasksViewModel(private val repository: TaskRepository) : ViewModel() {

    val tasks: StateFlow<List<Task>> = repository.getAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
    
    val stats: StateFlow<TaskStats> = tasks.map { list ->
        TaskStats(
            total = list.size,
            completed = list.count { it.isCompleted }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TaskStats(0, 0)
    )

    fun addTask(title: String, dueDate: Long?, notes: String?, priority: Int) {
        viewModelScope.launch {
            repository.insertTask(
                Task(
                    title = title,
                    dueDate = dueDate,
                    notes = notes,
                    priority = priority
                )
            )
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun restoreTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TomatoPlusApplication)
                TasksViewModel(application.container.taskRepository)
            }
        }
    }
}
