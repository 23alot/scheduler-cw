package com.boscatov.schedulercw.view.ui.fragment.project


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.view.adapter.project.ProjectAdapter
import com.boscatov.schedulercw.view.ui.dialog.new_project.NewProjectDialogFragment
import com.boscatov.schedulercw.view.viewmodel.project.ProjectViewModel
import kotlinx.android.synthetic.main.fragment_project.*


class ProjectFragment : Fragment(), ProjectAdapter.Callback {

    private lateinit var projectViewModel: ProjectViewModel
    private val adapter = ProjectAdapter(mutableListOf())
    private val receiver = UpdateReceiver()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_project, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectViewModel = ViewModelProviders.of(this).get(ProjectViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        projectViewModel.projects.observe(this, Observer {
            adapter.setProjects(it)
        })

        fragmentProjectRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ProjectFragment.adapter
        }

        fragmentProjectFAB.setOnClickListener {
            val dialog = NewProjectDialogFragment.getInstance()
            dialog.show(fragmentManager, "Add project")
        }

        adapter.setListener(this)

        projectViewModel.onLoadProjects()
    }

    override fun onDeleteProjectClick(position: Int) {
        projectViewModel.onDeleteProject(position)
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter("com.boscatov.schedulercw.updatelist")
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, filter)
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
        super.onPause()
    }

    inner class UpdateReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                when (it.action) {
                    "com.boscatov.schedulercw.updatelist" -> {
                        Handler().postDelayed(
                            {projectViewModel.onLoadProjects()}
                            , 200L
                        )
                    }
                    else -> return
                }
            }
        }
    }
}
