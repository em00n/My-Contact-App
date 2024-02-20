package com.emon.mycontactapp.base

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<D:ViewBinding> : AppCompatActivity(){

    protected lateinit var binding:D
    protected var activityContext: Activity? = null
    protected abstract fun viewBindingLayout(): D
    protected abstract fun initializeView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewBindingLayout()
        setContentView(binding.root)
        activityContext = this
        setStatusBarColor()
        initializeView(savedInstanceState)
    }

    private fun setStatusBarColor(color: Int = ContextCompat.getColor(this, android.R.color.white)) {

        try {
            this.window.statusBarColor = color
            WindowInsetsControllerCompat(
                this.window,
                this.window.decorView
            ).isAppearanceLightStatusBars = isLightColor(color)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun isLightColor(color: Int): Boolean {
        val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness < 0.5
    }
}