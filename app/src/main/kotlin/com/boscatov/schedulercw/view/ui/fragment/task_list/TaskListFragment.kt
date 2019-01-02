package com.boscatov.schedulercw.view.ui.fragment.task_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.interactor.task.TaskInteractor
import com.boscatov.schedulercw.view.adapter.task.TaskAdapter
import com.boscatov.schedulercw.view.viewmodel.holder.MainViewModel
import com.boscatov.schedulercw.view.viewmodel.task_list.TaskListViewModel
import kotlinx.android.synthetic.main.fragment_task_list.*
import javax.inject.Inject

class TaskListFragment : Fragment() {

    lateinit var mainViewModel: MainViewModel
    lateinit var taskListViewModel: TaskListViewModel
    private val taskListAdapter: TaskAdapter = TaskAdapter(arrayListOf())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        taskListViewModel = ViewModelProviders.of(this).get(TaskListViewModel::class.java)

        taskListFragmentRV.apply {
            layoutManager = LinearLayoutManager(this@TaskListFragment.context)
            adapter = taskListAdapter
        }

        taskListViewModel.tasks.observe(this, Observer<List<Task>> {
            taskListAdapter.setTasks(it)
        })
        taskListViewModel.loadData()
        taskListFragmentFAB.setOnClickListener {
            mainViewModel.onOpenNewTaskDialog()
            it.findNavController().navigate(R.id.action_taskListFragment_to_newTaskDialogFragment)
        }
    }
}