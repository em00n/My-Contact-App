package com.emon.mycontactapp.ui

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.emon.mycontactapp.R
import com.emon.mycontactapp.base.BaseFragment
import com.emon.mycontactapp.databinding.FragmentContactListBinding
import com.emon.mycontactapp.model.ContactListResult
import com.emon.mycontactapp.utils.AppConstants
import com.emon.mycontactapp.utils.autoCleared
import com.emon.mycontactapp.utils.hideKeyboard
import com.emon.mycontactapp.utils.navigateDestination
import com.emon.mycontactapp.utils.setUpVerticalRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class ContactListFragment : BaseFragment<FragmentContactListBinding>() {

    override fun viewBindingLayout(): FragmentContactListBinding =
        FragmentContactListBinding.inflate(layoutInflater)

    private val viewModel by viewModels<ContactListViewModel>()
    private var adapter by autoCleared<ContactListAdapter>()

    private var isDoubleBackPressToExit = false
    private var contactList: List<ContactListResult> = emptyList()
    private var contactQueryText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupOnBackPressed()
    }

    private fun setupOnBackPressed() {
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            handleBackPressed()
        }
        callback.isEnabled = true
    }

    private fun handleBackPressed() {
        if (isDoubleBackPressToExit) {
            requireActivity().finish()
        } else {
            isDoubleBackPressToExit = true
            showToastMessage(getString(R.string.message_app_exit))

            execute {
                delay(AppConstants.doublePressAppExitDelayTime)
                isDoubleBackPressToExit = false
            }
        }
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        viewModel.uiState bindTo ::handleUiState
        viewModel.action(ContactListUiAction.FetchContactListApi)

        adapter = ContactListAdapter(onContactClick = {
            navigateToContactDetails(it)
        })
        requireContext().setUpVerticalRecyclerView(binding.contactListRV, adapter)

        binding.contactSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.root.hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    performSearch(it)
                }
                return true
            }
        })
    }

    private fun navigateToContactDetails(contact: ContactListResult) {
        navigateDestination(
            ContactListFragmentDirections.actionNavigateToContactDetailsFragment(
                contact
            )
        )
    }

    fun performSearch(query: String) {
        contactQueryText = query
        val filteredList = contactList.filter { contact ->
            contact.full_name.contains(query, ignoreCase = true)
        }
        adapter.submitList(filteredList)
    }

    private fun handleUiState(state: ContactListUiState<Any>) {

        when (state) {
            is ContactListUiState.Loading -> {
                binding.incLoading.root.isVisible = state.isLoading
            }

            is ContactListUiState.ContactListApiSuccess -> {
                binding.incError.root.isVisible = false

                contactList = state.data
                if (contactQueryText.isEmpty()) adapter.submitList(contactList)
            }

            is ContactListUiState.ApiError -> {
                binding.incError.root.isVisible = true
                binding.incError.errorMessageTV.text = state.message
                binding.incError.tryAgainBtn.setOnClickListener {
                    viewModel.action(ContactListUiAction.FetchContactListApi)
                }
            }
        }
    }
}