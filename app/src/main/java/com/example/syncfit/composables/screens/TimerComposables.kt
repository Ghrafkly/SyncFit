package com.example.syncfit.composables.screens

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.syncfit.composables.custom.CustomNoPaddingTextField
import com.example.syncfit.composables.custom.CustomOutlinedTextField
import com.example.syncfit.database.models.Environment
import com.example.syncfit.database.models.Intensity
import com.example.syncfit.events.AppEvents
import com.example.syncfit.events.TimerEvents
import com.example.syncfit.states.AppState
import com.example.syncfit.ui.screens.ScreenConstants
import com.example.syncfit.ui.theme.Dimensions

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
) {
    var userInput by remember { mutableStateOf("") }

    Box(modifier = Modifier.padding(bottom = 5.dp)) {
        CustomOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            singleLine = true,
            value = userInput,
            onValueChange = {
                userInput = it
                onEvent(TimerEvents.UpdateTimerName(it))
            },
            label = { Text("Timer Name") }, /* TODO: Get timer name from database */
            trailingIcon = {
                IconButton(onClick = {
                    userInput = ""
                    onEvent(TimerEvents.UpdateTimerName(userInput))
                }) {
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
                    .padding(top = 45.dp)
                    .align(Alignment.BottomStart)
                    .offset { IntOffset(0, 40) },
            )
        }
    }

}

@Composable
fun Repeats(
    onEvent: (AppEvents) -> Unit,
) {
    var counter by remember { mutableIntStateOf(0) }

    Row(
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
                onEvent(TimerEvents.UpdateTimerRepeats(counter))
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
                onEvent(TimerEvents.UpdateTimerRepeats(counter))
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

@Composable
fun IntervalList(/* TODO: Pass in intervals */) {
    Text(text = "Intervals")
    LazyColumn(
        modifier = Modifier.height(210.dp),
    ) {
        val intervals = listOf(true, true, false) // TODO: Replace with actual data
        items(intervals.size) { index ->
            IntervalCard(intervals[index])
        }
    }
}

@Composable
fun IntervalCard(remove: Boolean) {
    var nameUserInput by remember { mutableStateOf("") }
    var timeUserInput by remember { mutableStateOf("") }

    val icon = if (remove) Icons.Default.DoNotDisturbOn else Icons.Default.AddCircle
    val tint = if (remove) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    val background = if (!remove) MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f) else Color.Transparent

    ListItem(
        leadingContent = {
            IconButton(
                onClick = { /*TODO: Link to timer runner screen*/ }
            ) {
                Icon(
                    imageVector = icon,
                    tint = tint,
                    contentDescription = "Start Timer",
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        },
        headlineContent = {
            if (!remove) {
                CustomNoPaddingTextField(
                    placeholder = { Text(text = "Name") },
                    value = nameUserInput,
                    onValueChange = { nameUserInput = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.primary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary,
                    ),
                )
            } else {
                Text(text = "Running")
            }
        },
        trailingContent = {
            if (!remove) {
                TextField(
                    placeholder = { Text(text = "00:00") },
                    value = timeUserInput,
                    onValueChange = { timeUserInput = it },
                    modifier = Modifier
                        .width(100.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                )
            } else {
                Text(
                    text = "01:00",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(16.dp)
                )
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = background,
        ),
    )
}

@Composable
fun Intensity(
    state: AppState,
    onEvent: (AppEvents) -> Unit,
) {
    var lowSelected by remember { mutableStateOf(false) }
    var highSelected by remember { mutableStateOf(false) }

    Text(text = "Intensity")
    Spacer(modifier = Modifier.height(Dimensions.Spacing.small))
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedButton(
            onClick = {
                lowSelected = true
                highSelected = false
                onEvent(TimerEvents.UpdateTimerIntensity(Intensity.LOW))
            },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor =
                if (lowSelected) {
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
                lowSelected = false
                highSelected = true
                onEvent(TimerEvents.UpdateTimerIntensity(Intensity.HIGH))
            },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor =
                if (highSelected) {
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
) {
    var indoorSelected by remember { mutableStateOf(false) }
    var outdoorSelected by remember { mutableStateOf(false) }
    var bothSelected by remember { mutableStateOf(false) }

    Text(text = "Environment")
    Spacer(modifier = Modifier.height(Dimensions.Spacing.small))
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedButton(
            onClick = {
                indoorSelected = true
                outdoorSelected = false
                bothSelected = false
                onEvent(TimerEvents.UpdateTimerEnvironment(Environment.INDOOR))
            },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor =
                if (indoorSelected) {
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
                indoorSelected = false
                outdoorSelected = true
                bothSelected = false
                onEvent(TimerEvents.UpdateTimerEnvironment(Environment.OUTDOOR))
            },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor =
                if (outdoorSelected) {
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
                indoorSelected = false
                outdoorSelected = false
                bothSelected = true
                onEvent(TimerEvents.UpdateTimerEnvironment(Environment.BOTH))
            },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor =
                if (bothSelected) {
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
