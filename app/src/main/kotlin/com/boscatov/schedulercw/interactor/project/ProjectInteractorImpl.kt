package com.boscatov.schedulercw.interactor.project

import com.boscatov.schedulercw.data.entity.Project
import com.boscatov.schedulercw.data.repository.project.ProjectRepository
import javax.inject.Inject

/**
 * Created by boscatov on 13.04.2019.
 */
class ProjectInteractorImpl @Inject constructor(
    private val projectRepository: ProjectRepository
): ProjectInteractor {
    override fun getProjects(): List<Project> {
        return projectRepository.getProjects()
    }

    override fun getProject(projectId: Long): Project {
        return projectRepository.getProject(projectId)
    }

    override fun saveProjects(projects: List<Project>) {
        projectRepository.saveProjects(projects)
    }

    override fun updateProject(project: Project) {
        projectRepository.updateProject(project)
    }

    override fun deleteProject(project: Project) {
        projectRepository.deleteProject(project)
    }
}
