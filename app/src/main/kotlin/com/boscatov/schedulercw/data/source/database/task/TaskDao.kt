package com.boscatov.schedulercw.data.source.database.task

import androidx.room.*
import com.boscatov.schedulercw.data.entity.Task
import java.util.Date

@Dao
interface TaskDao {

    @Query("SELECT * FROM task WHERE taskId IS :taskId")
    fun getTask(taskId: Long): Task

    @Query("SELECT * FROM task ORDER BY task_date_start")
    fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE task_status IN (:taskStatusesToShow)")
    fun getTasks(taskStatusesToShow: IntArray): List<Task>

    @Query("SELECT * FROM task WHERE taskId IN (:userIds) ORDER BY task_date_start")
    fun loadAllByIds(userIds: IntArray): List<Task>

    @Query("SELECT * FROM task WHERE task_date_start BETWEEN :from AND :to ORDER BY task_date_start")
    fun loadAllByDate(from: Date, to: Date): List<Task>

    @Query("SELECT * FROM task WHERE task_date_start BETWEEN :from AND :to ORDER BY task_date_start LIMIT 1")
    fun getNearestTask(from: Date, to: Date): Task

    @Query("SELECT * FROM task WHERE (task_status IN (:taskStatusesToShow)) OR (task_date_start BETWEEN :from AND :to) ORDER BY task_date_start DESC LIMIT 1")
    fun getLatestTask(taskStatusesToShow: IntArray, from: Date, to: Date): Task

    @Update
    fun update(task: Task)

    @Insert
    fun insertAll(vararg tasks: Task)

    @Delete
    fun delete(task: Task)
}