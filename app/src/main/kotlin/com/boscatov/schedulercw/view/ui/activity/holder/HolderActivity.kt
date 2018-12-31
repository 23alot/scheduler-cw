package com.boscatov.schedulercw.view.ui.activity.holder

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.view.ui.fragment.calendar.CalendarFragment
import com.boscatov.schedulercw.view.ui.fragment.task_list.TaskListFragment
import com.boscatov.schedulercw.view.viewmodel.holder.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_holder.*
import java.util.logging.Logger

class HolderActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holder)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        activityHolderBottomNV.setOnNavigationItemSelectedListener(this)
        initializeBottomNavigationView()
        setupToolbar()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bottomMenuCalendarAction -> navController.navigate(R.id.calendarFragment)
            R.id.bottomMenuHomeAction -> navController.navigate(R.id.taskListFragment)
//            R.id.bottomMenuStatsAction ->
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                activityHolderDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initializeBottomNavigationView() {
        val id = when(navController.currentDestination?.label) {
            TaskListFragment::class.java.simpleName -> R.id.bottomMenuHomeAction
            CalendarFragment::class.java.simpleName -> R.id.bottomMenuCalendarAction
            else -> R.id.bottomMenuHomeAction
        }
        activityHolderBottomNV.selectedItemId = id
    }

    private fun setupToolbar() {
        setSupportActionBar(activityHolderToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }
    }
}