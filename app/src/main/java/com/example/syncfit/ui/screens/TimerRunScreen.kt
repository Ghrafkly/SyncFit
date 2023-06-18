package com.example.syncfit.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.syncfit.SyncFitViewModel
import com.example.syncfit.composables.custom.CustomDivider
import com.example.syncfit.composables.custom.CustomNavBar
import com.example.syncfit.composables.custom.CustomTopAppBar
import com.example.syncfit.composables.custom.MainTopAppBar
import com.example.syncfit.database.models.TimerState
import com.example.syncfit.events.AppEvents
import com.example.syncfit.states.AppState
import com.example.syncfit.ui.theme.Dimensions

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TimerRunScreen(
    state : AppState,
    onEvent: (AppEvents) -> Unit,
    navController: NavController,
    viewModel: SyncFitViewModel,
) {
    val timerState = remember { mutableStateOf(TimerState.STOPPED) }

    val data by viewModel.state.collectAsState()
    var timer = data.timerState.timer.copy(
        timerName = data.timerState.timer.timerName,
        timerIntervals = data.timerState.timer.timerIntervals,
        timerRepeats = data.timerState.timer.timerRepeats,
        timerIntensity = data.timerState.timer.timerIntensity,
        timerEnvironment = data.timerState.timer.timerEnvironment,
    )

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = timer.timerName,
                onBackNavigateTo = { navController.popBackStack() },
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .consumeWindowInsets(innerPadding)
                    .padding(innerPadding)
                    .padding(top = Dimensions.Spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimensions.Spacing.large),
            ) {
                val totalRepeats by remember { mutableIntStateOf(timer.timerIntervals.size) }
                var currentRepeat by remember { mutableIntStateOf(1) }
                var intervalName by remember { mutableStateOf(timer.timerIntervals[0].intervalName) }

                Text(text = "Repeat: $currentRepeat / $totalRepeats") /*TODO: Get current repeat*/
                Text(text = "Interval: $intervalName") /*TODO: Get current interval*/
                Text(
                    text = "00:00", /*TODO: Get time and countdown*/
                    fontSize = MaterialTheme.typography.displayLarge.fontSize,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Button(
                        onClick = {
                            timerState.value = when (timerState.value) {
                                TimerState.RUNNING -> TimerState.PAUSED
                                TimerState.PAUSED -> TimerState.RUNNING
                                else -> { /*TODO: unknown*/ TimerState.STOPPED}
                            }
                        },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(text =
                        when (timerState.value) {
                            TimerState.RUNNING -> "Pause"
                            TimerState.PAUSED -> "Resume"
                            else -> {"Start"}
                        }
                        )
                    }
                    FilledTonalButton(
                        onClick = {
                            currentRepeat = 1
                            intervalName = timer.timerIntervals[0].intervalName
                        },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(text = "Restart")
                    }
                    Button(
                        onClick = { if (currentRepeat < totalRepeats) {
                            currentRepeat++
                            intervalName = timer.timerIntervals[currentRepeat - 1].intervalName
                        } },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        ),
                    ) {
                        Text(text = "Skip")
                    }
                }
                CustomDivider()
                Button(
                    onClick = { /*TODO: Stop timer*/ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                    ),
                    modifier = Modifier.width(Dimensions.ButtonWidth.medium),
                ) {
                    Text(text = "Stop")
                }
            }
        },
        bottomBar = { CustomNavBar(navController = navController) },
    )
}
