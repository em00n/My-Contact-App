package com.emon.mycontactapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.emon.mycontactapp.base.BaseListAdapter
import com.emon.mycontactapp.databinding.ItemContactListBinding
import com.emon.mycontactapp.model.ContactListResult
import com.emon.mycontactapp.utils.loadImage


class ContactListAdapter() : BaseListAdapter<ContactListResult, ItemContactListBinding>(
    diffCallback = object : DiffUtil.ItemCallback<ContactListResult>() {
        override fun areItemsTheSame(
            oldBiddingItem: ContactListResult,
            newBiddingItem: ContactListResult
        ): Boolean {
            return oldBiddingItem == newBiddingItem
        }

        override fun areContentsTheSame(
            oldBiddingItem: ContactListResult,
            newBiddingItem: ContactListResult
        ): Boolean {
            return oldBiddingItem == newBiddingItem
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ItemContactListBinding =
        ItemContactListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemContactListBinding, item: ContactListResult, position: Int) {
        binding.contactProfileIV.loadImage(item.image)
        binding.nameTV.text = item.full_name
        binding.mobileNumberTV.text = item.phone_number

    }
}