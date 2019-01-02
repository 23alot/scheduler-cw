package com.boscatov.schedulercw.view.ui.dialog.new_task

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.boscatov.schedulercw.R
import kotlinx.android.synthetic.main.dialog_new_task.*
import java.util.Calendar

class NewTaskDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private var currentPicker: TextView? = null

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
    }

    private fun initSpinner() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val minute = c.get(Calendar.MINUTE)
        val hour = c.get(Calendar.HOUR)
        dialogNewTaskStartDateTextView.text = "Date"

        dialogNewTaskStartDateTextView.setOnClickListener {
            val date = DatePickerDialog(activity, this, year, month, day)
            currentPicker = it as TextView
            date.show()
        }

        dialogNewTaskEndDateTextView.setOnClickListener {
            val date = DatePickerDialog(activity, this, year, month, day)
            currentPicker = it as TextView
            date.show()
        }

        dialogNewTaskStartTimeTextView.setOnClickListener {
            val time = TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
            currentPicker = it as TextView
            time.show()
        }

        dialogNewTaskEndTimeTextView.setOnClickListener {
            val time = TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
            currentPicker = it as TextView
            time.show()
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        Log.d("NewTaskDialog", "$day.$month.$year")
        currentPicker?.setText("$day.$month.$year")
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        Log.d("NewTaskDialog", "$minute:$hourOfDay")
        currentPicker?.setText("$minute:$hourOfDay")
    }
}