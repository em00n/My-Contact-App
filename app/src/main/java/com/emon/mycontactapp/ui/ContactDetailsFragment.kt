package com.emon.mycontactapp.ui

import android.os.Bundle
import androidx.navigation.fragment.navArgs
import com.emon.mycontactapp.base.BaseFragment
import com.emon.mycontactapp.databinding.FragmentContactDetailsBinding
import com.emon.mycontactapp.utils.loadImageWithoutCache
import com.emon.mycontactapp.utils.popBack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactDetailsFragment : BaseFragment<FragmentContactDetailsBinding>() {
    private val args by navArgs<ContactDetailsFragmentArgs>()

    override fun viewBindingLayout(): FragmentContactDetailsBinding = FragmentContactDetailsBinding.inflate(layoutInflater)

    override fun initializeView(savedInstanceState: Bundle?) {

        args.contact.let {
            binding.profileIV.loadImageWithoutCache(it.image)
            binding.nameTV.text = it.full_name
            binding.emailTV.text = it.email
            binding.numberTV.text = it.phone_number
        }
        binding.backIV.setOnClickListener {
            popBack()
        }
    }
}