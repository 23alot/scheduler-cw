package com.boscatov.schedulercw.view.adapter.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.data.entity.TaskStatus
import kotlinx.android.synthetic.main.task_item.view.*
import java.text.SimpleDateFormat

class TaskAdapter(val tasks: ArrayList<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    class TaskViewHolder(val task: CardView) : RecyclerView.ViewHolder(task)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val task = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false) as CardView
        return TaskViewHolder(task)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val dateFormatter = SimpleDateFormat("HH:mm")
        if (tasks[position].taskStatus == TaskStatus.DONE) {
            val color = holder.task.context.resources.getColor(R.color.colorTaskDone)
            holder.task.setCardBackgroundColor(color)
        } else {
            val color = holder.task.context.resources.getColor(R.color.ap_transparent)
            holder.task.setCardBackgroundColor(color)
        }
        tasks[position].taskDateStart?.let {
            holder.task.taskItemStartTimeTV.setText(dateFormatter.format(it))
        }

        tasks[position].taskDeadLine?.let {
            holder.task.taskItemDeadlineTV.setText(dateFormatter.format(it))
        } ?: run {
            holder.task.taskItemDeadlineTV.visibility = View.GONE
        }

        holder.task.taskItemEndTimeTV.setText("${tasks[position].taskDuration}")
        holder.task.taskItemTitleTV.setText(tasks[position].taskTitle)
        holder.task.taskItemSubtitleTV.setText(tasks[position].taskDescription)
        holder.task.taskItemColorLineIV.setBackgroundColor(tasks[position].taskColor)
    }

    override fun getItemCount(): Int = tasks.size

    fun setTasks(tasks: List<Task>) {
        this.tasks.clear()
        this.tasks.addAll(tasks)
        notifyDataSetChanged()
    }

}