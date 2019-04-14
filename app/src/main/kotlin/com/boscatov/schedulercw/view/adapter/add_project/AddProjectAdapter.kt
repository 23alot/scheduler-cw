package com.boscatov.schedulercw.view.adapter.add_project

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.data.entity.Project
import kotlinx.android.synthetic.main.add_project_item.view.*

/**
 * Created by boscatov on 14.04.2019.
 */
class AddProjectAdapter(val projects: MutableList<Project>) :
    RecyclerView.Adapter<AddProjectAdapter.AddProjectViewHolder>() {
    interface Callback {
        fun onProjectSelect(position: Int)
    }

    private var listener: Callback? = null

    class AddProjectViewHolder(val project: FrameLayout) : RecyclerView.ViewHolder(project)

    fun setOnProjectSelectListener(listener: Callback) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddProjectViewHolder {
        val task = LayoutInflater.from(parent.context).inflate(R.layout.add_project_item, parent, false) as FrameLayout
        return AddProjectViewHolder(task)
    }

    override fun onBindViewHolder(holder: AddProjectViewHolder, position: Int) {
        holder.project.addProjectItemTV.setText(projects[position].projectName)
        holder.project.setOnClickListener {
            listener?.onProjectSelect(position)
        }
    }

    override fun getItemCount(): Int {
        return projects.size
    }

    fun setTasks(projects: List<Project>) {
        val diffUtil = AddProjectDiffUtil(this.projects, projects)
        val result = DiffUtil.calculateDiff(diffUtil)
        this.projects.clear()
        this.projects.addAll(projects)
        result.dispatchUpdatesTo(this)
    }
}