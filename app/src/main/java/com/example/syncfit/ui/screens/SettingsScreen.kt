package com.example.syncfit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.syncfit.SyncFitViewModel
import com.example.syncfit.composables.custom.CustomDivider
import com.example.syncfit.composables.custom.CustomNavBar
import com.example.syncfit.composables.custom.MainTopAppBar
import com.example.syncfit.composables.screens.DeleteActions
import com.example.syncfit.composables.screens.SettingsActions
import com.example.syncfit.events.AppEvents
import com.example.syncfit.states.AppState
import com.example.syncfit.ui.theme.Dimensions

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    state : AppState,
    onEvent: (AppEvents) -> Unit,
    navController: NavController,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        topBar = {
            MainTopAppBar(
                title = "Settings"
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .consumeWindowInsets(innerPadding)
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SettingsActions()
                Spacer(modifier = Modifier.height(Dimensions.Spacing.large))
                CustomDivider()
                Spacer(modifier = Modifier.height(Dimensions.Spacing.large))
                DeleteActions(
                    state = state,
                    onEvent = onEvent,
                    navController = navController
                )
            }
        },
        bottomBar = { CustomNavBar(navController = navController)}
    )
}
