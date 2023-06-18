package com.example.syncfit.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
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
import com.example.syncfit.database.entities.Timer
import com.example.syncfit.database.models.*
import com.example.syncfit.events.AppEvents
import com.example.syncfit.events.TimerEvents
import com.example.syncfit.states.AppState
import com.example.syncfit.ui.theme.Dimensions
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TimerCreateScreen(
    state : AppState,
    onEvent: (AppEvents) -> Unit,
    navController: NavController,
    viewModel: SyncFitViewModel,
) {
    val focusManager = LocalFocusManager.current

    val data by viewModel.state.collectAsState()
    val timerValid = data.timerState.isTimerCreateSuccessful
    if (timerValid) {
        navController.navigate(ScreenConstants.Route.Timers.HOME)
        viewModel.resetCreate()
    }
    var timer = Timer()

    var timerName by remember { mutableStateOf(timer.timerName) }
    var timerRepeats by remember { mutableIntStateOf(timer.timerRepeats) }
    var timerIntensity by remember { mutableStateOf(timer.timerIntensity) }
    var timerEnvironment by remember { mutableStateOf(timer.timerEnvironment) }

    var firstEntry by remember { mutableStateOf(true) }
    if (firstEntry) {
        data.timerState.isTimerNameValid = true
        data.timerState.isTimerIntervalsValid = true
        data.timerState.isTimerRepeatsValid = true
        data.timerState.isTimerIntensityValid = true
        data.timerState.isTimerEnvironmentValid = true
        viewModel.resetCreate()
        firstEntry = false
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .clickable(
                interactionSource = MutableInteractionSource(),
                onClick = { focusManager.clearFocus() },
                indication = null
            ),
        topBar = {
            CustomTopAppBar(
                title = "Create Timer",
                iconButtons = {
                    IconButton(
                        onClick = {
                            timer = timer.copy(
                                timerName = timerName,
                                timerRepeats = timerRepeats,
                                timerIntensity = timerIntensity,
                                timerEnvironment = timerEnvironment,
                            )

                            onEvent(TimerEvents.UpdateTimer(timer, "create"))
                        },
                        modifier = Modifier.padding(end = Dimensions.Spacing.medium),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = "Save",
                        )
                    }
                },
                onBackNavigateTo = {
                    viewModel.resetCreate()
                    navController.navigate(ScreenConstants.Route.Timers.HOME)
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
                TimerName(
                    state = state,
                    onEvent = onEvent,
                    viewModel = viewModel,
                    name = timerName,
                    nameChange = { timerName = it },
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
                        repeats = timerRepeats,
                        repeatsChange = { timerRepeats = it },
                    )
                }
                CustomDivider(modifier = Modifier.padding(vertical = Dimensions.Spacing.medium))
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Intensity(
                        state = state, onEvent = onEvent,
                        viewModel = viewModel,
                        intensity = timerIntensity,
                        intensityChange = { timerIntensity = it },
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
                        environment = timerEnvironment,
                        environmentChange = { timerEnvironment = it },
                    )
                }
            }
        },
        bottomBar = { CustomNavBar(navController = navController) }
    )
}
