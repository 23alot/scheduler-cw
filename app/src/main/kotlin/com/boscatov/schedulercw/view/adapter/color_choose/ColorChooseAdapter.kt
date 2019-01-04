package com.boscatov.schedulercw.view.adapter.color_choose

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.spinner_color_choose.view.*

class ColorChooseAdapter(context: Context, val resource: Int) : ArrayAdapter<Int>(context, resource, colors) {
    companion object {
        private val colors = listOf(Color.BLACK, Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN)
    }

    private val inflater = LayoutInflater.from(context)

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(resource, parent, false)
        view.spinnerColorChooseFL.setBackgroundColor(colors[position])
        return view
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(resource, parent, false)
        view.spinnerColorChooseFL.setBackgroundColor(colors[position])
        return view
    }

    override fun getItem(position: Int): Int? {
        return colors[position]
    }

    override fun getCount(): Int = colors.size
}