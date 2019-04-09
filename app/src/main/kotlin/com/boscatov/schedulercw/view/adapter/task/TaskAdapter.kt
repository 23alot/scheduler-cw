package com.boscatov.schedulercw.view.adapter.task

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.RecyclerView
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.data.entity.TaskStatus
import kotlinx.android.synthetic.main.task_item.view.*
import java.text.SimpleDateFormat

class TaskAdapter(val tasks: MutableList<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
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

    // TODO: ;(
    class ItemTouch(context: Context) : ItemTouchHelper.Callback() {
        interface SwipeCallback {
            fun onChaosSwiped(position: Int)
            fun onDoneSwipe(position: Int)
        }
        companion object {
            private const val ICON_PADDING = 64
            private const val BACKGROUND_PADDING = 24
        }

        private var listener: SwipeCallback? = null

        private val chaosBitmap = with(context.resources.getDrawable(R.drawable.ic_chaos_24dp)) {
            setTint(Color.WHITE)
            toBitmap(96, 96)
        }
        private val doneBitmap = with(context.resources.getDrawable(R.drawable.ic_check_black_24dp)) {
            setTint(Color.WHITE)
            toBitmap(96, 96)
        }

        private val chaosBackground = context.resources.getColor(R.color.colorBackgroundTaskAbandoned)
        private val doneBackground = context.resources.getColor(R.color.colorBackgroundTaskDone)

        fun setListener(listener: SwipeCallback) {
            this.listener = listener
        }

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            return makeMovementFlags(0, LEFT or RIGHT)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            when(direction) {
                LEFT -> listener?.onDoneSwipe(viewHolder.adapterPosition)
                RIGHT -> listener?.onChaosSwiped(viewHolder.adapterPosition)
            }
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            if (Math.abs(dX) < viewHolder.itemView.width) {
                drawActions(c, viewHolder, dX)
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        private fun drawActions(canvas: Canvas, viewHolder: RecyclerView.ViewHolder, dX: Float) {
            if (dX > BACKGROUND_PADDING) {
                val view = viewHolder.itemView
                val rect = Rect(view.left + BACKGROUND_PADDING, view.top, dX.toInt() + view.left, view.bottom)

                val paint = Paint()
                paint.color = chaosBackground
                canvas.drawRect(rect, paint)
                if (dX.toInt() > ICON_PADDING) {
                    val bottom = view.top + (view.bottom - view.top + chaosBitmap.height) / 2
                    val width =
                        if (dX.toInt() - ICON_PADDING < chaosBitmap.width) dX.toInt() - ICON_PADDING else chaosBitmap.width
                    val bitmapRect = Rect(
                        view.left + ICON_PADDING,
                        bottom - chaosBitmap.height,
                        view.left + ICON_PADDING + width,
                        bottom
                    )
                    val src = Rect(0, 0, width, chaosBitmap.height)
                    canvas.drawBitmap(chaosBitmap, src, bitmapRect, null)
                }
            } else if (dX < -BACKGROUND_PADDING) {
                val view = viewHolder.itemView
                val rect = Rect(view.right + dX.toInt(), view.top, view.right - BACKGROUND_PADDING, view.bottom)

                val paint = Paint()
                paint.color = doneBackground
                canvas.drawRect(rect, paint)
                if (dX.toInt() < -ICON_PADDING) {
                    val bottom = view.top + (view.bottom - view.top + doneBitmap.height) / 2
                    val width =
                        if (Math.abs(dX.toInt()) - ICON_PADDING < doneBitmap.width) Math.abs(dX.toInt()) - ICON_PADDING else doneBitmap.width
                    val bitmapRect = Rect(
                        view.right - ICON_PADDING - width,
                        bottom - doneBitmap.height,
                        view.right - ICON_PADDING,
                        bottom
                    )
                    val src = Rect(doneBitmap.width - width, 0, doneBitmap.width, doneBitmap.height)
                    canvas.drawBitmap(doneBitmap, src, bitmapRect, null)
                }
            }
        }
    }
}