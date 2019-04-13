package com.boscatov.schedulercw.view.adapter.task

import androidx.recyclerview.widget.DiffUtil
import com.boscatov.schedulercw.data.entity.Task

/**
 * Created by boscatov on 13.04.2019.
 */

class TaskDiffUtil(
    private val oldList: List<Task>,
    private val newList: List<Task>
): DiffUtil.Callback() {
    override fun getNewListSize(): Int = newList.count()
    override fun getOldListSize(): Int = oldList.count()

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]

        return old.taskStatus == new.taskStatus && old.taskTitle == new.taskTitle &&
                old.taskDescription == new.taskDescription && old.taskDuration == new.taskDuration &&
                old.taskDateStart == new.taskDateStart
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return old.taskId == new.taskId
    }
}