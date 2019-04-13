package com.boscatov.schedulercw.data.repository.project

import com.boscatov.schedulercw.data.entity.Project

/**
 * Created by boscatov on 13.04.2019.
 */
interface ProjectRepository {
    fun getProjects(): List<Project>

    fun getProject(projectId: Long): Project

    fun saveProjects(projects: List<Project>)

    fun updateProject(project: Project)

    fun deleteProject(project: Project)
}
