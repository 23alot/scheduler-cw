package com.boscatov.schedulercw.view.viewmodel.add_parent_task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boscatov.schedulercw.data.entity.Project
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.data.entity.TaskStatus
import com.boscatov.schedulercw.di.Scopes
import com.boscatov.schedulercw.interactor.project.ProjectInteractor
import com.boscatov.schedulercw.interactor.task.TaskInteractor
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by boscatov on 14.04.2019.
 */

class AddParentTaskViewModel : ViewModel() {
    @Inject
    lateinit var taskInteractor: TaskInteractor

    init {
        val scope = Toothpick.openScope(Scopes.TASK_SCOPE)
        Toothpick.inject(this, scope)
    }
    val tasks = MutableLiveData<List<Task>>()
    val showTasks = MutableLiveData<List<Task>>()

    fun onLoadTasks() {
        taskInteractor.getTasks(arrayOf(TaskStatus.PENDING, TaskStatus.ACTIVE))
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                tasks.value = it
                showTasks.value = it
            }, {

            })
    }

    fun onTextChanged(mask: String) {
        showTasks.value = tasks.value?.filter { it.taskTitle.contains(mask) }
    }
}