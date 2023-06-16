package com.example.syncfit.composables.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.syncfit.SyncFitViewModel
import com.example.syncfit.events.AppEvents
import com.example.syncfit.events.TimerEvents
import com.example.syncfit.events.UserEvents
import com.example.syncfit.states.AppState
import com.example.syncfit.ui.screens.ScreenConstants
import com.example.syncfit.ui.theme.Dimensions
import kotlinx.coroutines.flow.collect

@Composable
fun SettingsActions() {
    var checked by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(0.8f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "Dark Mode")
        Switch(
            checked = checked,
            onCheckedChange = { checked = it },
        )
    }
}

@Composable
fun DeleteActions(
    state : AppState,
    onEvent: (AppEvents) -> Unit,
    navController: NavController,
) {
    Button(
        onClick = { /*onEvent(TimerEvents.DeleteTimers)*/ },
        modifier = Modifier.width(Dimensions.ButtonWidth.medium),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
        ),

        ) {
        Text(text = "Delete All Timers")
    }
    Spacer(modifier = Modifier.height(Dimensions.Spacing.medium))
    Button(
        onClick = {
            Log.i("SettingsComposables", "Delete Account: ${state.userState}")
            onEvent(UserEvents.DeleteUser)
            navController.navigate(ScreenConstants.Route.START)
        },
        modifier = Modifier.width(Dimensions.ButtonWidth.medium),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
        ),
    ) {
        Text(text = "Delete Account")
    }
}
