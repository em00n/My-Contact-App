package com.emon.mycontactapp.presentation.contactdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.emon.mycontactapp.domain.model.Contact
import com.emon.mycontactapp.presentation.contactlist.ContactListViewModel
import com.emon.mycontactapp.ui.components.LoadingScreen
import com.emon.mycontactapp.ui.theme.DarkGray
import com.emon.mycontactapp.ui.theme.LightGray
import com.emon.mycontactapp.ui.theme.MyContactAppTheme
import com.emon.mycontactapp.ui.theme.White
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ContactDetailsScreen(
    contactId: Int,
    viewModel: ContactListViewModel,
    onBackClick: () -> Unit
) {

    val contact = remember(contactId) {
        viewModel.getContact(contactId)
    }

    if (contact == null) {
        LoadingContent()
        return
    }

    ContactDetailsContent(
        contact = contact,
        onBackClick = onBackClick
    )
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
fun ContactDetailsContent(
    contact: Contact,
    onBackClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 13.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Back button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Image
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(contact.imageUrl)
                .diskCachePolicy(CachePolicy.DISABLED)   // ← disables disk cache
                .memoryCachePolicy(CachePolicy.DISABLED) // ← disables memory cache
                .crossfade(true)
                .build(),
            contentDescription = contact.fullName,
            modifier = Modifier
                .size(170.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = contact.fullName,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(text = contact.email, fontSize = 11.sp, color = DarkGray)

        Text(text = contact.phoneNumber, fontSize = 11.sp, color = DarkGray)

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {

            ActionButton(
                icon = Icons.Default.Call,
                label = "Call",
                modifier = Modifier.weight(1f)
            )

            ActionButton(
                icon = Icons.AutoMirrored.Filled.Message,
                label = "Message",
                modifier = Modifier.weight(1f)
            )

            ActionButton(
                icon = Icons.Default.Email,
                label = "Email",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ActionButton(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    Column(
        modifier = modifier
            .background(LightGray, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(icon, contentDescription = label)

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = label, fontSize = 13.sp)
    }
}

// Previews

@Preview
@Composable
fun ContactDetailsContentPreview() {

    val mockContact = Contact(
        id = 1,
        fullName = "John Doe",
        email = "john.doe@gmail.com",
        phoneNumber = "+880123456789",
        imageUrl = ""
    )

    MyContactAppTheme {
        ContactDetailsContent(
            contact = mockContact,
            onBackClick = {}
        )
    }
}
