package com.example.jettodoapp

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {
    //val id by mutableStateOf(0)
    var title by mutableStateOf("")
    var description by mutableStateOf("")

    var isShowDialog by mutableStateOf(false)

    private var editingTask: Task? = null

    // 編集モード中なのか、新規作成モード中なのか判別
    val isEditing: Boolean
        get() = editingTask != null

    fun setEditingTask(task: Task) {
        editingTask = task
        title = task.title
        description = task.description
    }

    // tasksがDBから受け取ったデータ
    // distinctUntilChanged DBに変更があっても中身が変わっていない場合は処理をしない
    val tasks = taskDao.loadAllTasks().distinctUntilChanged()

    fun createTask() {
        viewModelScope.launch {
            val newTask = Task(title = title, description = description)
            taskDao.insertTask(newTask)
            Log.d(MainViewModel::class.simpleName, "success create task")
        }
    }

    // ゴミ箱ボタンが押されたときにデータを削除する
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
        }
    }

    // 編集モード時に値を上書きする処理
    fun updateTask() {
        // editingTaskがnullかもしれないため、nullチェックを行う
        // let nullでない場合に実行
        editingTask?.let { task ->
            viewModelScope.launch {
                // 現在のタスクに上書きするような処理
                task.title = title
                task.description = description
                taskDao.updateTask(task)
            }
        }
    }

    // 編集モード解除時に内部のデータをリセットする
    fun resetProperties(){
        editingTask = null
        title = ""
        description = ""
    }
}