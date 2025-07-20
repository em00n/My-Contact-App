package com.emon.mycontactapp.presentation.contactlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.emon.mycontactapp.core.base.BaseListAdapter
import com.emon.mycontactapp.core.utils.loadImageWithoutCache
import com.emon.mycontactapp.databinding.ItemContactListBinding
import com.emon.mycontactapp.domain.model.Contact


class ContactListAdapter(
    private val onContactClick: (contact: Contact) -> Unit
) : BaseListAdapter<Contact, ItemContactListBinding>(
    diffCallback = object : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(
            oldBiddingItem: Contact,
            newBiddingItem: Contact
        ): Boolean {
            return oldBiddingItem.email == newBiddingItem.email
        }

        override fun areContentsTheSame(
            oldBiddingItem: Contact,
            newBiddingItem: Contact
        ): Boolean {
            return oldBiddingItem == newBiddingItem
        }
    }
) {
    override fun createBinding(parent: ViewGroup): ItemContactListBinding =
        ItemContactListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemContactListBinding, item: Contact, position: Int) {
        binding.contactProfileIV.loadImageWithoutCache(item.imageUrl)
        binding.nameTV.text = item.fullName
        binding.mobileNumberTV.text = item.phoneNumber

        binding.root.setOnClickListener {
            onContactClick.invoke(item)
        }
    }
}