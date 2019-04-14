package com.boscatov.schedulercw.view.adapter.project

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.data.entity.Project
import kotlinx.android.synthetic.main.project_item.view.*

/**
 * Created by boscatov on 14.04.2019.
 */
class ProjectAdapter(val projects: MutableList<Project>) :
    RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {
    interface Callback {
        fun onDeleteProjectClick(position: Int)
    }

    class ProjectViewHolder(val project: FrameLayout) : RecyclerView.ViewHolder(project)

    private var listener: Callback? = null

    fun setListener(listener: Callback) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val task = LayoutInflater.from(parent.context).inflate(R.layout.project_item, parent, false) as FrameLayout
        return ProjectViewHolder(task)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.project.projectItemTV.setText(projects[position].projectName)
        holder.project.projectItemTrashIV.setOnClickListener {
            listener?.onDeleteProjectClick(position)
        }

    }

    override fun getItemCount(): Int {
        return projects.size
    }

    fun setProjects(projects: List<Project>) {
        val diffUtil = ProjectDiffUtil(this.projects, projects)
        val result = DiffUtil.calculateDiff(diffUtil)
        this.projects.clear()
        this.projects.addAll(projects)
        result.dispatchUpdatesTo(this)
    }
}
