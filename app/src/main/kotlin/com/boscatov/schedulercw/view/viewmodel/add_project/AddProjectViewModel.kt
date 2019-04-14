package com.boscatov.schedulercw.view.viewmodel.add_project

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boscatov.schedulercw.data.entity.Project
import com.boscatov.schedulercw.di.Scopes
import com.boscatov.schedulercw.interactor.project.ProjectInteractor
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by boscatov on 14.04.2019.
 */

class AddProjectViewModel : ViewModel() {
    @Inject
    lateinit var projectInteractor: ProjectInteractor

    init {
        val scope = Toothpick.openScope(Scopes.TASK_SCOPE)
        Toothpick.inject(this, scope)
    }
    val projects = MutableLiveData<List<Project>>()
    val showProjects = MutableLiveData<List<Project>>()

    fun onLoadProjects() {
        Single.create<List<Project>> {
            val projects = projectInteractor.getProjects()
            it.onSuccess(projects)
        }.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                projects.value = it
                showProjects.value = it
            }, {

            })
    }

    fun onTextChanged(mask: String) {
        showProjects.value = projects.value?.filter { it.projectName.contains(mask) }
    }
}