package com.emon.mycontactapp.presentation.contactlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.emon.mycontactapp.core.utils.getContext
import com.emon.mycontactapp.domain.model.Contact
import com.emon.mycontactapp.ui.components.LoadingScreen
import com.emon.mycontactapp.ui.theme.DarkGray
import com.emon.mycontactapp.ui.theme.PantoneCoolGray
import com.emon.mycontactapp.ui.theme.WhiteSmoke
import com.emon.mycontactapp.R
import com.emon.mycontactapp.ui.theme.MyContactAppTheme

@Composable
fun ContactListScreen(
    onContactClick: (Contact) -> Unit,
    viewModel: ContactListViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    ContactListContent(
        uiState = uiState,
        onAction = viewModel::action,
        onContactClick = onContactClick
    )
}

@Composable
fun ContactListContent(
    uiState: ContactListUiState,
    onAction: (ContactListUiAction) -> Unit,
    onContactClick: (Contact) -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        when (uiState) {

            ContactListUiState.Loading -> {

                LoadingContent()
            }

            is ContactListUiState.Success -> {

                val filteredContacts = remember(
                    uiState.contacts,
                    uiState.searchQuery
                ) {

                    if (uiState.searchQuery.isBlank()) {
                        uiState.contacts
                    } else {
                        uiState.contacts.filter {
                            it.fullName.contains(uiState.searchQuery, ignoreCase = true)
                        }
                    }
                }

                SuccessContent(
                    searchQuery = uiState.searchQuery,
                    contacts = filteredContacts,
                    onSearchQueryChange = { query ->
                        onAction(
                            ContactListUiAction.OnSearchQueryChange(
                                query
                            )
                        )
                    },
                    onContactClick = onContactClick
                )
            }

            is ContactListUiState.Error -> {

                ErrorContent(
                    message = uiState.message,
                    onRetryClick = {
                        onAction(
                            ContactListUiAction.Retry
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun LoadingContent() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoadingScreen()
    }
}

@Composable
fun ErrorContent(
    message: String,
    onRetryClick: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = message,
                fontSize = 14.sp,
                color = DarkGray
            )

            Button(
                onClick = onRetryClick
            ) {
                Text("Try Again")
            }
        }
    }
}

@Composable
fun SuccessContent(
    searchQuery: String,
    contacts: List<Contact>,
    onSearchQueryChange: (String) -> Unit,
    onContactClick: (Contact) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ContactHeader()

        ContactSearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange
        )

        Spacer(modifier = Modifier.height(6.dp))

        ContactList(
            contacts = contacts,
            onContactClick = onContactClick
        )
    }
}

@Composable
fun ContactHeader() {

    Text(
        text = stringResource(R.string.app_name),
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.Black,
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {

    SearchBar(
        query = searchQuery,
        onQueryChange = onSearchQueryChange,
        onSearch = {},
        active = false,
        onActiveChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp),
        windowInsets = WindowInsets(top = 10.dp),
        placeholder = {
            Text("Search")
        }
    ) { }
}

@Composable
fun ContactList(
    contacts: List<Contact>,
    onContactClick: (Contact) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 13.dp,
                vertical = 13.dp
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(
            items = contacts,
            key = { it.phoneNumber }
        ) { contact ->

            ContactListItem(
                contact = contact,
                onContactClick = {
                    onContactClick(contact)
                }
            )
        }
    }
}

@Composable
fun ContactListItem(
    contact: Contact,
    onContactClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(WhiteSmoke)
            .clickable(onClick = onContactClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        AsyncImage(
            model = ImageRequest.Builder(getContext())
                .data(contact.imageUrl)
                .diskCachePolicy(CachePolicy.DISABLED)   // ← disables disk cache
                .memoryCachePolicy(CachePolicy.DISABLED) // ← disables memory cache
                .crossfade(true)
                .build(),
            contentDescription = contact.fullName,
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .weight(1f)
        ) {

            Text(
                text = contact.fullName,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = contact.email,
                fontSize = 12.sp,
                color = PantoneCoolGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// Previews

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ContactListContentLoadingPreview() {

    MyContactAppTheme {
        ContactListContent(
            uiState = ContactListUiState.Loading,
            onAction = {},
            onContactClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ContactListContentSuccessPreview() {

    val contacts = listOf(
        Contact(
            id = 1,
            fullName = "John Doe",
            email = "john@gmail.com",
            phoneNumber = "+880123456789",
            imageUrl = ""
        ),
        Contact(
            id = 2,
            fullName = "Emma Watson",
            email = "emma@gmail.com",
            phoneNumber = "+880987654321",
            imageUrl = ""
        )
    )

    MyContactAppTheme {
        ContactListContent(
            uiState = ContactListUiState.Success(
                contacts = contacts,
                searchQuery = ""
            ),
            onAction = {},
            onContactClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ContactListContentErrorPreview() {

    MyContactAppTheme {
        ContactListContent(
            uiState = ContactListUiState.Error(
                message = "Something went wrong"
            ),
            onAction = {},
            onContactClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorContentPreview() {

    MyContactAppTheme {
        ErrorContent(
            message = "Unable to fetch contacts",
            onRetryClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SuccessContentPreview() {

    val contacts = listOf(
        Contact(
            id = 1,
            fullName = "John Doe",
            email = "john@gmail.com",
            phoneNumber = "+880123456789",
            imageUrl = ""
        ),
        Contact(
            id = 2,
            fullName = "Emma Watson",
            email = "emma@gmail.com",
            phoneNumber = "+880987654321",
            imageUrl = ""
        )
    )

    MyContactAppTheme {
        SuccessContent(
            searchQuery = "",
            contacts = contacts,
            onSearchQueryChange = {},
            onContactClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContactHeaderPreview() {

    MyContactAppTheme {
        ContactHeader()
    }
}

@Preview(showBackground = true)
@Composable
fun ContactSearchBarPreview() {

    MyContactAppTheme {
        ContactSearchBar(
            searchQuery = "John",
            onSearchQueryChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContactListPreview() {

    val contacts = listOf(
        Contact(
            id = 1,
            fullName = "John Doe",
            email = "john@gmail.com",
            phoneNumber = "+880123456789",
            imageUrl = ""
        ),
        Contact(
            id = 2,
            fullName = "Emma Watson",
            email = "emma@gmail.com",
            phoneNumber = "+880987654321",
            imageUrl = ""
        )
    )

    MyContactAppTheme {
        ContactList(
            contacts = contacts,
            onContactClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContactListItemPreview() {

    val contact = Contact(
        id = 1,
        fullName = "John Doe",
        email = "john@gmail.com",
        phoneNumber = "+880123456789",
        imageUrl = ""
    )

    MyContactAppTheme {
        ContactListItem(
            contact = contact,
            onContactClick = {}
        )
    }
}