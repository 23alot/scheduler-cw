package com.boscatov.schedulercw.view.adapter.add_parent_task

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.data.entity.Project
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.view.adapter.add_project.AddProjectAdapter
import com.boscatov.schedulercw.view.adapter.add_project.AddProjectDiffUtil
import com.boscatov.schedulercw.view.adapter.task.TaskDiffUtil
import kotlinx.android.synthetic.main.add_project_item.view.*

/**
 * Created by boscatov on 14.04.2019.
 */
class AddParentTaskAdapter(val tasks: MutableList<Task>) :
    RecyclerView.Adapter<AddParentTaskAdapter.AddParentTaskViewHolder>() {
    interface Callback {
        fun onTaskSelect(task: Task)
    }

    private var listener: Callback? = null

    class AddParentTaskViewHolder(val task: FrameLayout) : RecyclerView.ViewHolder(task)

    fun setOnProjectSelectListener(listener: Callback) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddParentTaskViewHolder {
        val task = LayoutInflater.from(parent.context).inflate(R.layout.add_project_item, parent, false) as FrameLayout
        return AddParentTaskViewHolder(task)
    }

    override fun onBindViewHolder(holder: AddParentTaskViewHolder, position: Int) {
        holder.task.addProjectItemTV.setText(tasks[position].taskTitle)
        holder.task.setOnClickListener {
            listener?.onTaskSelect(tasks[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun getItemId(position: Int): Long {
        return tasks[position].taskId
    }

    fun setTasks(tasks: List<Task>) {
        val diffUtil = TaskDiffUtil(this.tasks, tasks)
        val result = DiffUtil.calculateDiff(diffUtil)
        this.tasks.clear()
        this.tasks.addAll(tasks)
        result.dispatchUpdatesTo(this)
    }
}
