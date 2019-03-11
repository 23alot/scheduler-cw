package com.boscatov.schedulercw.view.ui.fragment.chaos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.view.adapter.task.TaskAdapter
import com.boscatov.schedulercw.view.viewmodel.chaos.ChaosViewModel
import com.boscatov.schedulercw.view.viewmodel.holder.MainViewModel
import kotlinx.android.synthetic.main.fragment_chaos.*

class ChaosFragment : Fragment() {

    lateinit var mainViewModel: MainViewModel
    lateinit var chaosViewModel: ChaosViewModel

    private val chaosListAdapter: TaskAdapter = TaskAdapter(arrayListOf())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chaos, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        chaosViewModel = ViewModelProviders.of(this).get(ChaosViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentChaosTasksRV.apply {
            layoutManager = LinearLayoutManager(this@ChaosFragment.context)
            adapter = chaosListAdapter
        }
        chaosViewModel.tasks.observe(this, Observer {
            chaosListAdapter.setTasks(it)
        })

        chaosViewModel.onLoadTasks()
        fragmentChaosSortTasksFAB.setOnClickListener { chaosViewModel.onPredictTasks() }
    }
}