package com.example.jettodoapp

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * '@Insert'でDBへ保存する
 * '@Query'でDBから、すべてのタスクを取得する
 * '@Update'でDBに保存されているタスクを更新する
 * '@Delete'でDBに保存されているタスク情報を削除する
 * suspendで非同期ワンショットクエリ
 * Flow<>でオブザーバルクエリで非同期化
 */
@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM Task")
    fun loadAllTasks(): Flow<List<Task>>

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}