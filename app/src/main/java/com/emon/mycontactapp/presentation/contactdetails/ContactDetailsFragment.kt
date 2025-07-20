package com.emon.mycontactapp.presentation.contactdetails

import android.os.Bundle
import androidx.navigation.fragment.navArgs
import com.emon.mycontactapp.core.base.BaseFragment
import com.emon.mycontactapp.databinding.FragmentContactDetailsBinding
import com.emon.mycontactapp.core.utils.loadImageWithoutCache
import com.emon.mycontactapp.core.utils.popBack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactDetailsFragment : BaseFragment<FragmentContactDetailsBinding>() {
    private val args by navArgs<ContactDetailsFragmentArgs>()

    override fun viewBindingLayout(): FragmentContactDetailsBinding = FragmentContactDetailsBinding.inflate(layoutInflater)

    override fun initializeView(savedInstanceState: Bundle?) {

        args.contact.let {
            binding.profileIV.loadImageWithoutCache(it.imageUrl)
            binding.nameTV.text = it.fullName
            binding.emailTV.text = it.email
            binding.numberTV.text = it.phoneNumber
        }
        binding.backIV.setOnClickListener {
            popBack()
        }
    }
}