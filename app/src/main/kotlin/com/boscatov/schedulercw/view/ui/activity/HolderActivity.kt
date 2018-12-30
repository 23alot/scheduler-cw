package com.boscatov.schedulercw.view.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.view.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_holder.*
import java.security.AccessControlContext

class HolderActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holder)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }
}