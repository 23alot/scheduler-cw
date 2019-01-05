package com.boscatov.schedulercw.view.ui.dialog.new_task

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
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
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.view.adapter.color_choose.ColorChooseAdapter
import com.boscatov.schedulercw.view.ui.state.NewTaskAcceptState
import com.boscatov.schedulercw.view.ui.state.State
import com.boscatov.schedulercw.view.viewmodel.holder.MainViewModel
import com.boscatov.schedulercw.view.viewmodel.new_task.NewTaskViewModel
import kotlinx.android.synthetic.main.dialog_new_task.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

class NewTaskDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private var currentPicker: TextView? = null
    lateinit var mainViewModel: MainViewModel
    lateinit var newTaskViewModel: NewTaskViewModel

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
        initSpinner()
        mainViewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        mainViewModel.state.observe(this, Observer {
            changeState(it)
        })
        newTaskViewModel = ViewModelProviders.of(this).get(NewTaskViewModel::class.java)
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

        return Task(
            taskTitle = title,
            taskDescription = description,
            taskColor = color,
            taskDateStart = startDate,
            taskDuration = duration,
            taskTimeStart = startTime,
            taskPriority = priority
        )
    }

    private fun initSpinner() {
        val c = Calendar.getInstance(TimeZone.getDefault())
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val minute = c.get(Calendar.MINUTE)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        dialogNewTaskStartDateTextView.text = "Date"

        dialogNewTaskStartDateTextView.setOnClickListener {
            val date = DatePickerDialog(activity, this, year, month, day)
            currentPicker = it as TextView
            date.show()
        }

        dialogNewTaskStartTimeTextView.setOnClickListener {
            val time = TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
            currentPicker = it as TextView
            time.show()
        }

        dialogNewTaskDurationSpinner.setOnClickListener {
            val time = TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
            currentPicker = it as TextView
            time.show()
        }

        val spinnerAdapter = ColorChooseAdapter(context!!, R.layout.spinner_color_choose)
        dialogNewTaskColorChooseSpinner.adapter = spinnerAdapter
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        Log.d("NewTaskDialog", "$day.$month.$year")
        val date = Calendar.getInstance()
        date.set(year, month, day)
        val format = SimpleDateFormat.getDateInstance()
        currentPicker?.setText(format.format(date.time))
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        Log.d("NewTaskDialog", "$hourOfDay:$minute")
        val date = Calendar.getInstance()
        date.set(Calendar.MINUTE, minute)
        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
        val format = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
        currentPicker?.setText(format.format(date.time))
    }
}