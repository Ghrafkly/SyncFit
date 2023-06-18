package com.example.syncfit.composables.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoNotDisturbOn
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.syncfit.composables.custom.CustomNoPaddingTextField
import com.example.syncfit.composables.custom.CustomOutlinedTextField
import com.example.syncfit.composables.custom.CustomTimePicker
import com.example.syncfit.database.models.Environment
import com.example.syncfit.database.models.Intensity
import com.example.syncfit.database.models.Interval
import com.example.syncfit.events.AppEvents
import com.example.syncfit.events.TimerEvents
import com.example.syncfit.fromTimeStamp
import com.example.syncfit.states.AppState
import com.example.syncfit.toTimeStamp
import com.example.syncfit.ui.screens.ScreenConstants
import com.example.syncfit.ui.theme.Dimensions
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.syncfit.SyncFitViewModel

@Composable
fun TimerCard(
    modifier: Modifier = Modifier,
    onEvent: (AppEvents) -> Unit,
    navController: NavController,
) {
    var favourite by remember { mutableStateOf(false) }

    ListItem(
        modifier = Modifier.clickable { navController.navigate(ScreenConstants.Route.Timers.DETAILS) },
        headlineContent = { Text("Leg Squats") },
        supportingContent = {
            Text("1m45s - 3 sets - 10 reps")
        },
        leadingContent = {
            IconButton(onClick = { favourite = !favourite }) {
                Icon(
                    imageVector = when (favourite) {
                        true -> Icons.Filled.Favorite
                        false -> Icons.Outlined.FavoriteBorder
                    },
                    contentDescription = "Favourite",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        trailingContent = {
            IconButton(onClick = { navController.navigate(ScreenConstants.Route.Timers.RUN) }) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Start",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            }
        },
    )
    Divider()
}

@Composable
fun TimersViewActions(
    modifier: Modifier = Modifier,
    onEvent: (AppEvents) -> Unit,
    onCreateNavigateTo: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimensions.Spacing.large),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.width(Dimensions.ButtonWidth.small),
        ) {
            Text(text = "Filter")
        }
        FilledTonalButton(
            onClick = { onCreateNavigateTo() },
            modifier = Modifier.width(Dimensions.ButtonWidth.small),
        ) {
            Text(text = "Create Timer")
        }
    }
}

@Composable
fun TimerName(
    state: AppState,
    onEvent: (AppEvents) -> Unit,
    viewModel: SyncFitViewModel,
    name: String,
    nameChange: (String) -> Unit,
) {
    var userInput by remember { mutableStateOf(name) }

    Box(modifier = Modifier.padding(bottom = 5.dp)) {
        CustomOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            singleLine = true,
            value = userInput,
            onValueChange = {
                userInput = it
                println("User input: $userInput | It: $it")
                nameChange(it)
            },
            label = { Text("Timer Name") }, /* TODO: Get timer name from database */
            trailingIcon = {
                IconButton(onClick = { userInput = "" }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear")
                }
            },
            isError = !state.timerState.isTimerNameValid
        )
        if (!state.timerState.isTimerNameValid) {
            Text(
                text = "Timer name cannot be empty",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                ),
                modifier = Modifier
                    .padding(top = 40.dp)
                    .align(Alignment.BottomStart)
                    .offset { IntOffset(0, 40) },
            )
        }
    }

}

@Composable
fun Repeats(
    onEvent: (AppEvents) -> Unit,
    viewModel: SyncFitViewModel,
    state: AppState,
    repeats: Int,
    repeatsChange: (Int) -> Unit,
) {
    val data by viewModel.state.collectAsState()
    val validRepeat = data.timerState.isTimerRepeatsValid
//
    var counter by remember { mutableIntStateOf(repeats) }

    val annotatedString = buildAnnotatedString {
        append("Repeats")
        if (!validRepeat) {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.error,
                )
            ) {
                append(" 1 or more")
            }
        }
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            modifier = Modifier.weight(2f),
            text = annotatedString
        )
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = counter.toString(),
                modifier = Modifier
            )
            Spacer(modifier = Modifier.width(Dimensions.Spacing.small))
            IconButton(
                onClick = {
                    if (counter > 0) counter--
                    repeatsChange(counter)
                },
                modifier = Modifier
                    .width(40.dp)
                    .size(27.dp),
                content = {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = "Reduce"
                    )
                }
            )
            IconButton(
                onClick = {
                    counter++
                    repeatsChange(counter)
                },
                modifier = Modifier
                    .width(40.dp)
                    .size(27.dp),
                content = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Increase"
                    )
                }
            )
        }
    }

}

@Composable
fun IntervalList(
    modifier: Modifier = Modifier,
    onEvent: (AppEvents) -> Unit,
    state: AppState,
    viewModel: SyncFitViewModel,
) {
    val data by viewModel.state.collectAsState()
    val emptyInterval = data.timerState.isTimerIntervalsValid
    val intervals by viewModel.timerIntervals.collectAsState()

    val annotatedString = buildAnnotatedString {
        append("Intervals")
        if (!emptyInterval) {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.error,
                )
            ) {
                append(" Add Interval")
            }
        }
    }

    Text(text = annotatedString)
    LazyColumn(
        modifier = Modifier.height(210.dp),
    ) {
        if (intervals.isEmpty()) {
            item {
                DefaultIntervalCard(
                    onEvent = onEvent,
                    state = state,
                    addInterval = { onEvent(TimerEvents.AddTimerInterval(it)) },
                )
            }
        } else {
            itemsIndexed(intervals) { index, interval ->
                IntervalCard(
                    onEvent = onEvent,
                    state = state,
                    interval = interval,
                    deleteInterval = { onEvent(TimerEvents.DeleteTimerInterval(it)) },
                )

                if (index == intervals.size - 1) {
                    DefaultIntervalCard(
                        onEvent = onEvent,
                        state = state,
                        addInterval = {
                            println("Adding interval: $it")
                            onEvent(TimerEvents.AddTimerInterval(it))
                          },
                    )
                }
            }
        }

    }
}

@Composable
fun DefaultIntervalCard(
    modifier: Modifier = Modifier,
    onEvent: (AppEvents) -> Unit,
    state: AppState,
    addInterval: (Interval) -> Unit,
) {
    var intervalName by remember { mutableStateOf("") }
    var intervalTime by remember { mutableStateOf("00:00") }

    var isIntervalValid by remember { mutableStateOf(true) }

    var showTimePicker by remember { mutableStateOf(false) }

    if (showTimePicker) {
        CustomTimePicker(
            onDismissRequest = { showTimePicker = false },
            onConfirm = { intervalTime = it },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(300.dp),
            onEvent = onEvent,
            state = state,
        )
    }

    ListItem(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                color = if (!isIntervalValid) MaterialTheme.colorScheme.error else Color.Unspecified,
                shape = RoundedCornerShape(10.dp)
            ),
        leadingContent = {
            IconButton(
                onClick = {
                    if (intervalName.isNotEmpty() && intervalTime.isNotEmpty()) {
                        isIntervalValid = true
                        addInterval(
                            Interval(
                                intervalName = intervalName,
                                intervalTimeStamp = intervalTime.toTimeStamp(),
                            )
                        )
                    } else {
                        isIntervalValid = false
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Add Timer",
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        },
        headlineContent = {
            CustomNoPaddingTextField(
                placeholder = { Text(text = "Name") },
                value = intervalName,
                onValueChange = { intervalName = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.primary,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
        trailingContent = {
            Text(
                modifier = Modifier
                    .clickable { showTimePicker = true },
                text = intervalTime,
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
        ),

    )
}

@Composable
fun IntervalCard(
    modifier: Modifier = Modifier,
    onEvent: (AppEvents) -> Unit,
    state: AppState,
    interval: Interval,
    deleteInterval: (Interval) -> Unit,
) {
    val intervalName = interval.intervalName
    val intervalTime = interval.intervalTimeStamp.fromTimeStamp()

    ListItem(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp)),
        leadingContent = {
            IconButton(
                onClick = { deleteInterval(interval) }
            ) {
                Icon(
                    imageVector = Icons.Default.DoNotDisturbOn,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = "Delete Interval",
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        },
        headlineContent = { Text(text = intervalName) },
        trailingContent = { Text(text = intervalTime) },
    )
}

@Composable
fun Intensity(
    state: AppState,
    onEvent: (AppEvents) -> Unit,
    viewModel: SyncFitViewModel,
    intensity: Intensity,
    intensityChange: (Intensity) -> Unit,
) {
    val data by viewModel.state.collectAsState()
    val validIntensity = data.timerState.isTimerIntensityValid

    var selected by remember { mutableStateOf(intensity) }

    val annotatedString = buildAnnotatedString {
        append("Intensity")
        if (!validIntensity) {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.error,
                )
            ) {
                append(" Select Intensity")
            }
        }
    }

    Text(text = annotatedString)
    Spacer(modifier = Modifier.height(Dimensions.Spacing.small))
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedButton(
            onClick = {
                selected = Intensity.LOW
                intensityChange(selected)
            },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor =
                if (selected == Intensity.LOW) {
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                } else {
                    Color.Transparent
                },
            ),
            modifier = Modifier.weight(1f),
        ) {
            Text(text = Intensity.LOW.name)
        }
        OutlinedButton(
            onClick = {
                selected = Intensity.HIGH
                intensityChange(selected)
            },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor =
                if (selected == Intensity.HIGH) {
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                } else {
                    Color.Transparent
                },
            ),
            modifier = Modifier.weight(1f),
        ) {
            Text(text = Intensity.HIGH.name)
        }
    }
}

@Composable
fun Environment(
    onEvent: (AppEvents) -> Unit,
    viewModel: SyncFitViewModel,
    environment: Environment,
    environmentChange: (Environment) -> Unit,
) {
    val data by viewModel.state.collectAsState()
    val validEnvironment = data.timerState.isTimerEnvironmentValid

    var selected by remember { mutableStateOf(environment) }

    val annotatedString = buildAnnotatedString {
        append("Environment")
        if (!validEnvironment) {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.error,
                )
            ) {
                append(" Select Environment")
            }
        }
    }

    Text(text = annotatedString)
    Spacer(modifier = Modifier.height(Dimensions.Spacing.small))
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedButton(
            onClick = {
                selected = Environment.INDOOR
                environmentChange(selected)
            },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor =
                if (selected == Environment.INDOOR) {
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                } else {
                    Color.Transparent
                },
            ),
            modifier = Modifier.weight(1f),
        ) {
            Text(text = Environment.INDOOR.name)
        }
        OutlinedButton(
            onClick = {
                selected = Environment.OUTDOOR
                environmentChange(selected)
            },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor =
                if (selected == Environment.OUTDOOR) {
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                } else {
                    Color.Transparent
                },
            ),
            modifier = Modifier.weight(1f),
        ) {
            Text(text = Environment.OUTDOOR.name)
        }
        OutlinedButton(
            onClick = {
                selected = Environment.BOTH
                environmentChange(selected)
            },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor =
                if (selected == Environment.BOTH) {
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                } else {
                    Color.Transparent
                },
            ),
            modifier = Modifier.weight(1f),
        ) {
            Text(text = Environment.BOTH.name)
        }
    }
}
