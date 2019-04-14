package com.boscatov.schedulercw.view.ui.dialog.add_project

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.data.entity.Project
import com.boscatov.schedulercw.view.viewmodel.ProjectViewModel

/**
 * Created by boscatov on 14.04.2019.
 */

class AddProjectDialogFragment : DialogFragment() {
    companion object {
        fun getInstance(): DialogFragment {
            return AddProjectDialogFragment()
        }
    }

    private lateinit var projectViewModel: ProjectViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(R.layout.dialog_add_project)
            .setPositiveButton("ok") { _, _ ->
//                val project = Project(projectName = dialogAdd)
//                projectViewModel.onAddProject()
            }.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectViewModel = ViewModelProviders.of(this).get(ProjectViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_add_project, container, false)
    }
}