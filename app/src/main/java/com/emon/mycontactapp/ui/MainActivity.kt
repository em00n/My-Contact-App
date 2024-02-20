package com.emon.mycontactapp.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.emon.mycontactapp.R
import com.emon.mycontactapp.base.BaseActivity
import com.emon.mycontactapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun viewBindingLayout(): ActivityMainBinding  = ActivityMainBinding.inflate(layoutInflater)

    private lateinit var navController: NavController
    override fun initializeView(savedInstanceState: Bundle?) {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostContainerView) as NavHostFragment
        navController = navHostFragment.navController

    }
}