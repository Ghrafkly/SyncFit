package com.example.syncfit.composables.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.ViewAgenda
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.syncfit.composables.custom.CustomDivider
import com.example.syncfit.events.AppEvents
import com.example.syncfit.ui.theme.Dimensions
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun WelcomeMessage(user: String) {
    Text(text = "Welcome, $user!")
}

@Composable
fun ProfileImage() {
    Box {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Default Profile Picture",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(150.dp)
                    .clip(CircleShape),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileActions(
    modifier: Modifier = Modifier,
    onEvent: (AppEvents) -> Unit,
    onEditNavigateTo: () -> Unit,
    onLogOutNavigateTo: () -> Unit,
    clickGoogleSignOut: () -> Job,
) {
    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = { false },
    )
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.Spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimensions.Spacing.medium)
    ) {
        Card(
            onClick = { scope.launch { sheetState.show() } },
            content = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.Spacing.medium),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Upload Image")
                    Icon(
                        imageVector = Icons.Outlined.Image,
                        contentDescription = "Upload Image",
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        )
        Card(
            onClick = { onEditNavigateTo() },
            content = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.Spacing.medium),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Edit Profile")
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Upload Image",
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        )
        CustomDivider()
        Button(
            onClick = {
                clickGoogleSignOut()
                onLogOutNavigateTo()
            },
            modifier = Modifier
                .width(Dimensions.ButtonWidth.large),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.background
            ),
            content = {
                Text(text = "Log Out")
            }
        )
    }

    if (sheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = { scope.launch { sheetState.hide() } },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.Spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimensions.Spacing.medium)
            ) {
                Button(
                    onClick = { /*TODO: Upload image*/ },
                    modifier = Modifier
                        .width(Dimensions.ButtonWidth.medium),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onBackground,
                        contentColor = MaterialTheme.colorScheme.background
                    ),
                    content = {
                        Text(text = "Upload Image")
                    }
                )
                Button(
                    onClick = { /*TODO: Take picture*/ },
                    modifier = Modifier
                        .width(Dimensions.ButtonWidth.medium),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onBackground,
                        contentColor = MaterialTheme.colorScheme.background
                    ),
                    content = {
                        Text(text = "Take Picture")
                    }
                )
            }
        }
    }
}
