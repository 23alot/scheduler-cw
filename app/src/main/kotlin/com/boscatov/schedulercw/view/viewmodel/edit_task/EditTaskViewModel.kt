package com.boscatov.schedulercw.view.viewmodel.edit_task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boscatov.schedulercw.data.entity.Project
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.di.Scopes
import com.boscatov.schedulercw.interactor.project.ProjectInteractor
import com.boscatov.schedulercw.interactor.task.TaskInteractor
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import javax.inject.Inject

class EditTaskViewModel : ViewModel() {
    @Inject
    lateinit var taskInteractor: TaskInteractor
    @Inject
    lateinit var projectInteractor: ProjectInteractor
    init {
        val scope = Toothpick.openScope(Scopes.TASK_SCOPE)
        Toothpick.inject(this, scope)
    }
    val task: MutableLiveData<Task> = MutableLiveData()
    val parentTask: MutableLiveData<Task> = MutableLiveData()
    val project: MutableLiveData<Project> = MutableLiveData()

    fun onAcceptNewTask(task: Task) {
        Observable.fromCallable {
            taskInteractor.updateTask(task)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
        }
    }

    fun onLoadTask(taskId: Long) {
        Single.fromCallable {
            taskInteractor.getTask(taskId)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe (
            {
                task.value = it
            },
            {

            }
        )
    }

    fun onLoadParent(taskId: Long) {
        Single.fromCallable {
            taskInteractor.getTask(taskId)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe (
            {
                parentTask.value = it
            },
            {

            }
        )
    }

    fun onLoadProject(projectId: Long) {
        Single.fromCallable {
            projectInteractor.getProject(projectId)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe (
            {
                project.value = it
            },
            {

            }
        )
    }
}