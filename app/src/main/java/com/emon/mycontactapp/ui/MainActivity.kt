package com.emon.mycontactapp.ui

import android.os.Bundle
import androidx.navigation.NavController
import com.emon.mycontactapp.base.BaseActivity
import com.emon.mycontactapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun viewBindingLayout(): ActivityMainBinding  = ActivityMainBinding.inflate(layoutInflater)

    private lateinit var navController: NavController
    override fun initializeView(savedInstanceState: Bundle?) {

    }
}