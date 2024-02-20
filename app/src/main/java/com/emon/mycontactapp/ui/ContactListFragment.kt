package com.emon.mycontactapp.ui

import android.os.Bundle
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import com.emon.mycontactapp.R
import com.emon.mycontactapp.base.BaseFragment
import com.emon.mycontactapp.databinding.FragmentContactListBinding
import com.emon.mycontactapp.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class ContactListFragment : BaseFragment<FragmentContactListBinding>() {

    override fun viewBindingLayout(): FragmentContactListBinding = FragmentContactListBinding.inflate(layoutInflater)

    private val viewModel by viewModels<ContactListViewModel>()
    private var isDoubleBackPressToExit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback {

            if (isDoubleBackPressToExit) {
                requireActivity().finish()
                return@addCallback
            }

            isDoubleBackPressToExit = true
            showToastMessage(getString(R.string.message_app_exit))

            execute {
                delay(AppConstants.doublePressAppExitDelayTime)
                isDoubleBackPressToExit = false
            }
        }
        callback.isEnabled = true
    }

    override fun initializeView(savedInstanceState: Bundle?) {

    }
}