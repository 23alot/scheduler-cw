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
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.view.adapter.task.TaskAdapter
import com.boscatov.schedulercw.view.viewmodel.holder.MainViewModel
import com.boscatov.schedulercw.view.viewmodel.task_list.TaskListViewModel
import kotlinx.android.synthetic.main.fragment_task_list.*
import java.text.SimpleDateFormat
import java.util.Date

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
            val id = taskListViewModel.getCurrentTaskId()
            id?.let {
                if(it >= 0) {
                    taskListFragmentRV.layoutManager?.scrollToPosition(it)
                }
            }
        })
        taskListViewModel.day.observe(this, Observer {
            changeTitle(it)
            taskListViewModel.loadData()
        })
        taskListFragmentFAB.setOnClickListener {
            mainViewModel.onOpenNewTaskDialog()
            it.findNavController().navigate(R.id.action_taskListFragment_to_newTaskDialogFragment)
        }

        taskListFragmentDayBackIB.setOnClickListener {
            taskListViewModel.decreaseDate()
        }
        taskListFragmentDayForwardIB.setOnClickListener {
            taskListViewModel.increaseDate()
        }
        mainViewModel.state.observe(this, Observer {
            taskListViewModel.loadData()
        })
    }

    private fun changeTitle(date: Date) {
        val formatter = SimpleDateFormat("dd")
        taskListFragmentTitle.setText(formatter.format(date))
    }
}