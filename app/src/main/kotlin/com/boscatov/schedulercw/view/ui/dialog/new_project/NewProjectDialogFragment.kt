package com.boscatov.schedulercw.view.ui.dialog.new_project

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.data.entity.Project
import com.boscatov.schedulercw.view.viewmodel.ProjectViewModel
import kotlinx.android.synthetic.main.dialog_new_project.*

/**
 * Created by boscatov on 14.04.2019.
 */
class NewProjectDialogFragment : DialogFragment() {
    companion object {
        fun getInstance(): DialogFragment {
            return NewProjectDialogFragment()
        }
    }

    private lateinit var projectViewModel: ProjectViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(R.layout.dialog_new_project)
            .setPositiveButton("ok") { _, _ ->
                val editText = dialog.findViewById<EditText>(R.id.dialogNewProjectET)
                val project = Project(projectName = editText.text.toString())
                projectViewModel.onAddProject(project)
                val intent = Intent("com.boscatov.schedulercw.updatelist")
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectViewModel = ViewModelProviders.of(this).get(ProjectViewModel::class.java)
    }
}