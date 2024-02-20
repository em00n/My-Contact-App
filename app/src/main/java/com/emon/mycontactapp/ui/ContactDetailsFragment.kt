package com.emon.mycontactapp.ui

import android.os.Bundle
import androidx.navigation.fragment.navArgs
import com.emon.mycontactapp.base.BaseFragment
import com.emon.mycontactapp.databinding.FragmentContactDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactDetailsFragment : BaseFragment<FragmentContactDetailsBinding>() {
    private val args by navArgs<ContactDetailsFragmentArgs>()

    override fun viewBindingLayout(): FragmentContactDetailsBinding = FragmentContactDetailsBinding.inflate(layoutInflater)

    override fun initializeView(savedInstanceState: Bundle?) {

    }
}