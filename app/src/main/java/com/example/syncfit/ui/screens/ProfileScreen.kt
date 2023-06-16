package com.example.syncfit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.syncfit.SyncFitViewModel
import com.example.syncfit.composables.custom.CustomNavBar
import com.example.syncfit.composables.custom.MainTopAppBar
import com.example.syncfit.composables.screens.ProfileActions
import com.example.syncfit.composables.screens.ProfileImage
import com.example.syncfit.composables.screens.WelcomeMessage
import com.example.syncfit.events.AppEvents
import com.example.syncfit.states.AppState
import kotlinx.coroutines.Job

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    state : AppState,
    onEvent: (AppEvents) -> Unit,
    navController: NavController,
    clickGoogleSignOut: () -> Job,
) {
    Scaffold(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface),
        topBar = {
            MainTopAppBar(
                title = "Profile",
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .consumeWindowInsets(innerPadding)
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                ProfileImage()
                WelcomeMessage(user = "USER")
                ProfileActions(
                    onEvent = onEvent,
                    navController = navController,
                    clickGoogleSignOut = clickGoogleSignOut
                )
            }
        },
        bottomBar = { CustomNavBar(navController = navController) }
    )
}
