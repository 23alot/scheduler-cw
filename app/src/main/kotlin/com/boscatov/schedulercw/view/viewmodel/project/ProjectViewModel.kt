package com.boscatov.schedulercw.view.viewmodel.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boscatov.schedulercw.data.entity.Project
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.di.Scopes
import com.boscatov.schedulercw.interactor.project.ProjectInteractor
import com.boscatov.schedulercw.interactor.task.TaskInteractor
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by boscatov on 13.04.2019.
 */

class ProjectViewModel : ViewModel() {
    @Inject
    lateinit var taskInteractor: TaskInteractor

    @Inject
    lateinit var projectInteractor: ProjectInteractor

    val projects = MutableLiveData<List<Project>>()
    init {
        val scope = Toothpick.openScope(Scopes.TASK_SCOPE)
        Toothpick.inject(this, scope)
    }

    fun onLoadProjects() {
        Single.create<List<Project>> {
            it.onSuccess(projectInteractor.getProjects())
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it ->
                projects.value = it
            }
    }

    fun onAddProject(project: Project) {
        Completable.create {
            projectInteractor.saveProjects(listOf(project))
            it.onComplete()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                onLoadProjects()
            }
    }

    fun onDeleteProject(position: Int) {
        Completable.create {
            projectInteractor.deleteProject(projects.value!![position])
            it.onComplete()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                onLoadProjects()
            }
    }
}