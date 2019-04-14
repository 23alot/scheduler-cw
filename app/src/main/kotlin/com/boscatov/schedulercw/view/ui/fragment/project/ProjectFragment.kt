package com.boscatov.schedulercw.view.ui.fragment.project


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.view.viewmodel.ProjectViewModel


class ProjectFragment : Fragment() {

    private lateinit var projectViewModel: ProjectViewModel

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

        })
    }

}
