package com.boscatov.schedulercw.view.ui.activity.holder

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.work.WorkManager
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.view.ui.fragment.calendar.CalendarFragment
import com.boscatov.schedulercw.view.ui.fragment.stats.StatsFragment
import com.boscatov.schedulercw.view.ui.fragment.task_list.TaskListFragment
import com.boscatov.schedulercw.view.ui.state.DefaultState
import com.boscatov.schedulercw.view.ui.state.NewTaskState
import com.boscatov.schedulercw.view.ui.state.State
import com.boscatov.schedulercw.view.viewmodel.holder.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_holder.*

class HolderActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holder)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        activityHolderBottomNV.setOnNavigationItemSelectedListener(this)
        mainViewModel.state.observe(this, Observer {
            changeState(it)
        })
        initializeBottomNavigationView()
        setupToolbar()
        initWorkers()
    }

    // TODO: Перенести в Preferences
    private fun initWorkers() {
        Log.d("MainViewModel", "init0")
        WorkManager.getInstance().cancelAllWork()
        WorkManager.getInstance().cancelAllWorkByTag(MainViewModel.TASK_WORKER_TAG)

        Log.d("MainViewModel", "${WorkManager.getInstance().cancelAllWork().result}")
        WorkManager.getInstance().getWorkInfosByTagLiveData(MainViewModel.TASK_WORKER_TAG).observe(this, Observer {
            Log.d("MainViewModel", "init ${it.size}")
            if (it.size != 5) {
                Log.d("MainViewModel", "init")
                mainViewModel.initNotificationWorker()
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bottomMenuCalendarAction -> navController.navigate(R.id.calendarFragment)
            R.id.bottomMenuHomeAction -> navController.navigate(R.id.taskListFragment)
            R.id.bottomMenuStatsAction -> navController.navigate(R.id.statsFragment)
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
        val id = when (navController.currentDestination?.label) {
            TaskListFragment::class.java.simpleName -> R.id.bottomMenuHomeAction
            CalendarFragment::class.java.simpleName -> R.id.bottomMenuCalendarAction
            StatsFragment::class.java.simpleName -> R.id.bottomMenuStatsAction
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

    private fun changeState(state: State) {
        when(state) {
            is DefaultState -> changeToDefault()
            is NewTaskState -> changeToNewTask()
        }
    }

    private fun changeToDefault() {

    }

    private fun changeToNewTask() {
        activityHolderBottomNV.visibility = View.GONE
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)
        }
    }
}