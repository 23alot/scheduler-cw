package com.boscatov.schedulercw.data.source.database.project

import androidx.room.Dao
import androidx.room.Query
import com.boscatov.schedulercw.data.entity.Project

/**
 * Created by boscatov on 13.04.2019.
 */
@Dao
interface ProjectDao {
    @Query("SELECT * FROM project")
    fun getAll(): List<Project>

    @Query("SELECT * FROM project WHERE projectId IS :projectId")
    fun getProjectById(projectId: Long): Project
}