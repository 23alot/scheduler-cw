package com.boscatov.schedulercw.data.repository.project

import com.boscatov.schedulercw.data.entity.Project
import com.boscatov.schedulercw.data.source.database.project.ProjectDatabase
import javax.inject.Inject

/**
 * Created by boscatov on 13.04.2019.
 */
class ProjectRepositoryImpl @Inject constructor(
    private val database: ProjectDatabase
) : ProjectRepository {
    override fun getProjects(): List<Project> {
        return database.projectDatabase().getAll()
    }

    override fun getProject(projectId: Long): Project {
        return database.projectDatabase().getProjectById(projectId)
    }

    override fun saveProjects(projects: List<Project>) {
        database.projectDatabase().insertAll(*projects.toTypedArray())
    }

    override fun updateProject(project: Project) {
        database.projectDatabase().update(project)
    }

    override fun deleteProject(project: Project) {
        database.projectDatabase().delete(project)
    }
}