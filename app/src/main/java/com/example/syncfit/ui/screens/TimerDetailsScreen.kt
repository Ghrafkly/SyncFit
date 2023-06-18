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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.syncfit.SyncFitViewModel
import com.example.syncfit.composables.custom.CustomDivider
import com.example.syncfit.composables.custom.CustomNavBar
import com.example.syncfit.composables.custom.CustomTopAppBar
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
fun TimerDetailsScreen(
    state : AppState,
    onEvent: (AppEvents) -> Unit,
    navController: NavController,
    viewModel: SyncFitViewModel,
) {
    var edit by remember { mutableStateOf(false) }

    val data by viewModel.state.collectAsState()
    val timer = data.timerState.timer.copy(
        timerName = data.timerState.timer.timerName,
        timerIntervals = data.timerState.timer.timerIntervals,
        timerRepeats = data.timerState.timer.timerRepeats,
        timerIntensity = data.timerState.timer.timerIntensity,
        timerEnvironment = data.timerState.timer.timerEnvironment,
    )

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Details",
                iconButtons = {
                    IconButton(
                        onClick = { edit = !edit },
                    ) {
                        Icon(
                            imageVector = if (edit) Icons.Filled.Save else Icons.Filled.ModeEdit,
                            contentDescription = if (edit) "Save" else "Edit",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    IconButton(
                        onClick = { navController.navigate(ScreenConstants.Route.Timers.RUN) },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Play",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = { navController.navigate(ScreenConstants.Route.Timers.HOME) },
                        modifier = Modifier.padding(end = Dimensions.Spacing.medium),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                onBackNavigateTo = { navController.navigate(ScreenConstants.Route.Timers.HOME) }
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
                TimerName(
                    state = state,
                    onEvent = onEvent,
                    viewModel = viewModel,
                    name = timer.timerName,
                    nameChange = { timer.timerName = it },
                )
                CustomDivider(modifier = Modifier.padding(vertical = Dimensions.Spacing.medium))
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    horizontalAlignment = Alignment.Start,
                ) {
                    IntervalList(
                        state = state,
                        onEvent = onEvent,
                        viewModel = viewModel,
                    )
                }
                CustomDivider(modifier = Modifier.padding(vertical = Dimensions.Spacing.medium))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Repeats(
                        onEvent = onEvent,
                        state = state,
                        viewModel = viewModel,
                        repeats = timer.timerRepeats,
                        repeatsChange = { timer.timerRepeats = it },
                    )
                }
                CustomDivider(modifier = Modifier.padding(vertical = Dimensions.Spacing.medium))
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Intensity(
                        state = state,
                        onEvent = onEvent,
                        viewModel = viewModel,
                        intensity = timer.timerIntensity,
                        intensityChange = { timer.timerIntensity = it },
                    )
                }
                CustomDivider(modifier = Modifier.padding(vertical = Dimensions.Spacing.medium))
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Environment(
                        onEvent = onEvent,
                        viewModel = viewModel,
                        environment = timer.timerEnvironment,
                        environmentChange = { timer.timerEnvironment = it },
                    )
                }
            }
        },
        bottomBar = { CustomNavBar(navController = navController) }
    )
}
