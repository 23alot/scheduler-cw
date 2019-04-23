package com.boscatov.schedulercw.view.ui.dialog.new_task

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.data.entity.TaskStatus
import com.boscatov.schedulercw.view.adapter.color_choose.ColorChooseAdapter
import com.boscatov.schedulercw.view.ui.dialog.add_parent.AddParentTaskDialogFragment
import com.boscatov.schedulercw.view.ui.dialog.add_project.AddProjectDialogFragment
import com.boscatov.schedulercw.view.ui.state.NewTaskAcceptState
import com.boscatov.schedulercw.view.ui.state.State
import com.boscatov.schedulercw.view.viewmodel.holder.MainViewModel
import com.boscatov.schedulercw.view.viewmodel.new_task.NewTaskViewModel
import kotlinx.android.synthetic.main.dialog_new_task.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

class NewTaskDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    lateinit var mainViewModel: MainViewModel
    lateinit var newTaskViewModel: NewTaskViewModel
    private val receiver = AddReceiver()
    private var taskParentId: Long = -1
    private var projectId: Long = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_new_task, null)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)
        builder.setView(activity?.layoutInflater?.inflate(R.layout.dialog_new_task, null))
        return builder.create()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        mainViewModel.state.observe(this, Observer {
            changeState(it)
        })
        newTaskViewModel = ViewModelProviders.of(this).get(NewTaskViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
        dialogNewTaskAllowAutoTimeCB.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dialogNewTaskDeadlineGroup.visibility = View.VISIBLE
                dialogNewTaskDateGroup.visibility = View.GONE
            } else {
                dialogNewTaskDeadlineGroup.visibility = View.GONE
                dialogNewTaskDateGroup.visibility = View.VISIBLE
            }
        }

        dialogNewTaskParentTaskTV.setOnClickListener {
            val dialog = AddParentTaskDialogFragment.getInstance()
            dialog.show(fragmentManager, "AddParentTaskDialogFragment")
        }

        dialogNewTaskProjectTV.setOnClickListener {
            val dialog = AddProjectDialogFragment.getInstance()
            dialog.show(fragmentManager, "AddProjectDialogFragment")
        }
    }

    private fun changeState(state: State) {
        if (state is NewTaskAcceptState) {
            saveNewTask()
        }
    }

    private fun saveNewTask() {
        val task = getTask()
        newTaskViewModel.onAcceptNewTask(task)
        mainViewModel.onNewTaskComplete()
    }

    private fun getTask(): Task {
        val title = dialogNewTaskTitleETV.text.toString()
        val description = dialogNewTaskDescriptionETV.text.toString()
        val startDate = dialogNewTaskStartDateTextView.text.toString()
        val startTime = dialogNewTaskStartTimeTextView.text.toString()
        val duration = dialogNewTaskDurationSpinner.text.toString().toInt()
        val radioButton = view?.findViewById<RadioButton>(dialogNewTaskChoosePriorityRG.checkedRadioButtonId)
        val priority = radioButton?.text.toString().toInt()
        val color = dialogNewTaskColorChooseSpinner.selectedItem as Int
        val deadlineDate = dialogNewTaskDeadlineDateTV.text.toString()
        val deadlineTime = dialogNewTaskDeadlineTimeTV.text.toString()
        val parentTask = if (taskParentId == -1L) null else taskParentId
        val project = if (projectId == -1L) null else projectId


        if (dialogNewTaskAllowAutoTimeCB.isChecked) {
            val dateFormat = SimpleDateFormat("dd MMMM, yyyy").parse(deadlineDate)
            val timeFormat = SimpleDateFormat("HH:mm").parse(deadlineTime)
            val date = Calendar.getInstance()
            date.time = dateFormat
            val time = Calendar.getInstance()
            time.time = timeFormat

            date.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY))
            date.set(Calendar.MINUTE, time.get(Calendar.MINUTE))

            return Task(
                taskTitle = title,
                taskDescription = description,
                taskColor = color,
                taskDuration = duration,
                taskPriority = priority,
                taskDeadLine = date.time,
                taskStatus = TaskStatus.ABANDONED,
                taskParentId = parentTask,
                taskProjectId = project
            )
        } else {
            val dateFormat = SimpleDateFormat("dd MMMM, yyyy").parse(startDate)
            val timeFormat = SimpleDateFormat("HH:mm").parse(startTime)
            val date = Calendar.getInstance()
            date.time = dateFormat
            val time = Calendar.getInstance()
            time.time = timeFormat

            date.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY))
            date.set(Calendar.MINUTE, time.get(Calendar.MINUTE))

            return Task(
                taskTitle = title,
                taskDescription = description,
                taskColor = color,
                taskDateStart = date.time,
                taskDuration = duration,
                taskPriority = priority,
                taskParentId = parentTask,
                taskProjectId = project
            )
        }
    }

    private fun initSpinner() {
        val c = Calendar.getInstance(TimeZone.getDefault())
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val minute = c.get(Calendar.MINUTE)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMMM, yyyy")
        dialogNewTaskStartDateTextView.setText(dateFormat.format(calendar.time))
        dialogNewTaskDeadlineDateTV.setText(dateFormat.format(calendar.time))

        dialogNewTaskStartDateTextView.setOnClickListener {
            val date = DatePickerDialog(activity, this, year, month, day)
            date.show()
        }

        dialogNewTaskStartTimeTextView.setOnClickListener {
            val time = TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
            time.show()
        }

        dialogNewTaskDeadlineDateTV.setOnClickListener {
            val date = DatePickerDialog(activity, this, year, month, day)
            date.show()
        }

        dialogNewTaskDeadlineTimeTV.setOnClickListener {
            val time = TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
            time.show()
        }

        val timeFormat = SimpleDateFormat("HH:mm")
        dialogNewTaskStartTimeTextView.setText(timeFormat.format(calendar.time))
        dialogNewTaskDeadlineTimeTV.setText(timeFormat.format(calendar.time))

        val spinnerAdapter = ColorChooseAdapter(context!!, R.layout.spinner_color_choose)
        dialogNewTaskColorChooseSpinner.adapter = spinnerAdapter
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        Log.d("NewTaskDialog", "$day.$month.$year")
        val date = Calendar.getInstance()
        date.set(year, month, day)
        val format = SimpleDateFormat("dd MMMM, yyyy")
        // TODO: убрать это
        if (dialogNewTaskDateGroup.isVisible) {
            dialogNewTaskStartDateTextView.setText(format.format(date.time))
        } else {
            dialogNewTaskDeadlineDateTV.setText(format.format(date.time))
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        Log.d("NewTaskDialog", "$hourOfDay:$minute")
        val date = Calendar.getInstance()
        date.set(Calendar.MINUTE, minute)
        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
        val format = SimpleDateFormat("HH:mm")
        // TODO: убрать это
        if (dialogNewTaskDateGroup.isVisible) {
            dialogNewTaskStartTimeTextView.setText(format.format(date.time))
        } else {
            dialogNewTaskDeadlineTimeTV.setText(format.format(date.time))
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        filter.addAction("com.boscatov.schedulercw.add_project")
        filter.addAction("com.boscatov.schedulercw.add_parent_task")
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, filter)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }

    inner class AddReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                when (it.action) {
                    "com.boscatov.schedulercw.add_project" -> {
                        val id = it.getLongExtra("PROJECT_ID", 0)
                        val name = it.getStringExtra("PROJECT_NAME")
                        projectId = id
                        dialogNewTaskProjectTV.setText(name)
                    }
                    "com.boscatov.schedulercw.add_parent_task" -> {
                        val id = it.getLongExtra("TASK_ID", 0)
                        val title = it.getStringExtra("TASK_TITLE")
                        taskParentId = id
                        dialogNewTaskParentTaskTV.setText(title)
                    }
                    else -> return
                }
            }
        }
    }
}