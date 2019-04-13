package com.boscatov.schedulercw.data.source.database.project

import androidx.room.*
import com.boscatov.schedulercw.data.entity.Project
import com.boscatov.schedulercw.data.entity.Task

/**
 * Created by boscatov on 13.04.2019.
 */
@Dao
interface ProjectDao {
    @Query("SELECT * FROM project")
    fun getAll(): List<Project>

    @Query("SELECT * FROM project WHERE projectId IS :projectId")
    fun getProjectById(projectId: Long): Project

    @Update
    fun update(project: Project)

    @Insert
    fun insertAll(vararg projects: Project)

    @Delete
    fun delete(project: Project)
}