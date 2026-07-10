package com.emon.mycontactapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.emon.mycontactapp.presentation.contactdetails.ContactDetailsScreen
import com.emon.mycontactapp.presentation.contactlist.ContactListScreen
import com.emon.mycontactapp.presentation.contactlist.ContactListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = ContactListRoute
    ) {
        composable<ContactListRoute> {
            ContactListScreen(
                onContactClick = { contact ->
                    navController.navigate(ContactDetailsRoute(contact.id))
                }
            )
        }

        composable<ContactDetailsRoute> { backStackEntry ->

            val args = backStackEntry.toRoute<ContactDetailsRoute>()
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry<ContactListRoute>()
            }
            val viewModel: ContactListViewModel = koinViewModel(viewModelStoreOwner = parentEntry)

            ContactDetailsScreen(
                contactId = args.id,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}