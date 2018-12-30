package com.boscatov.schedulercw.view.ui.fragment.task_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.view.viewmodel.holder.MainViewModel
import com.boscatov.schedulercw.view.viewmodel.task_list.TaskListViewModel

class TaskListFragment : Fragment() {

    lateinit var mainViewModel: MainViewModel
    lateinit var taskListViewModel: TaskListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        taskListViewModel = ViewModelProviders.of(this).get(TaskListViewModel::class.java)
    }
}