package com.example.syncfit.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.syncfit.SyncFitViewModel
import com.example.syncfit.composables.custom.CustomDivider
import com.example.syncfit.composables.custom.CustomNavBar
import com.example.syncfit.composables.custom.MainTopAppBar
import com.example.syncfit.composables.screens.Environment
import com.example.syncfit.composables.screens.Intensity
import com.example.syncfit.composables.screens.IntervalList
import com.example.syncfit.composables.screens.Repeats
import com.example.syncfit.composables.screens.TimerName
import com.example.syncfit.events.AppEvents
import com.example.syncfit.states.AppState
import com.example.syncfit.ui.theme.Dimensions

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TimerCreateScreen(
    state : AppState,
    onEvent: (AppEvents) -> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = {
            MainTopAppBar(
                title = "Create Timer",
                iconButtons = {
                    IconButton(
                        onClick = { /*TODO: Save details*/ },
                        modifier = Modifier.padding(end = Dimensions.Spacing.medium),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = "Save",
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .consumeWindowInsets(innerPadding)
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(top = Dimensions.Spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TimerName()
                CustomDivider(modifier = Modifier.padding(vertical = Dimensions.Spacing.medium))
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    horizontalAlignment = Alignment.Start,
                ) {
                    IntervalList()
                }
                CustomDivider(modifier = Modifier.padding(vertical = Dimensions.Spacing.medium))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "Repeats")
                    Repeats()
                }
                CustomDivider(modifier = Modifier.padding(vertical = Dimensions.Spacing.medium))
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Intensity()
                }
                CustomDivider(modifier = Modifier.padding(vertical = Dimensions.Spacing.medium))
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Environment()
                }
            }
        },
        bottomBar = { CustomNavBar(navController = navController) }
    )
}
