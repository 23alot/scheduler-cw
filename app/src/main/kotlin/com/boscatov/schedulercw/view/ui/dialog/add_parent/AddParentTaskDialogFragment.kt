package com.boscatov.schedulercw.view.ui.dialog.add_parent

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.view.adapter.add_parent_task.AddParentTaskAdapter
import com.boscatov.schedulercw.view.viewmodel.add_parent_task.AddParentTaskViewModel
import kotlinx.android.synthetic.main.dialog_add_project.*

/**
 * Created by boscatov on 14.04.2019.
 */

class AddParentTaskDialogFragment : DialogFragment(), AddParentTaskAdapter.Callback {
    companion object {
        fun getInstance(): DialogFragment {
            return AddParentTaskDialogFragment()
        }
    }

    private lateinit var taskViewModel: AddParentTaskViewModel
    private val adapter = AddParentTaskAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel = ViewModelProviders.of(this).get(AddParentTaskViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_add_project, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskViewModel.showTasks.observe(this, Observer {
            adapter.setTasks(it)
        })

        dialogAddProjectRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AddParentTaskDialogFragment.adapter
        }
        dialogAddProjectET.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                taskViewModel.onTextChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
        })
        dialogAddProjectET.hint = "Task title"
        adapter.setOnProjectSelectListener(this)
        taskViewModel.onLoadTasks()
    }

    override fun onTaskSelect(task: Task) {
        val intent = Intent("com.boscatov.schedulercw.add_parent_task")
        intent.putExtra("TASK_ID", task.taskId)
        intent.putExtra("TASK_TITLE", task.taskTitle)
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        dismiss()
    }

    override fun onResume() {
        super.onResume()
        dialog.window?.setLayout(800, 1200)
    }
}