package com.boscatov.schedulercw.view.ui.fragment.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.view.viewmodel.holder.MainViewModel
import com.boscatov.schedulercw.view.viewmodel.stats.StatsViewModel

class StatsFragment : Fragment() {

    lateinit var mainViewModel: MainViewModel
    lateinit var statsViewModel: StatsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        statsViewModel = ViewModelProviders.of(this).get(StatsViewModel::class.java)
    }
}