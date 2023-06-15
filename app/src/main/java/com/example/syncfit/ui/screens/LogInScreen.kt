package com.example.syncfit.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.example.syncfit.composables.custom.CustomDivider
import com.example.syncfit.composables.screens.LogInMaster
import com.example.syncfit.events.AppEvents
import com.example.syncfit.states.AppState
import com.example.syncfit.ui.theme.Dimensions
import kotlinx.coroutines.Job

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LogInScreen(
    modifier: Modifier = Modifier,
    type: String,
    onEvent: (AppEvents) -> Unit,
    onBackNavigateTo: () -> Unit,
    onLinkNavigateTo: () -> Unit,
    onLogInNavigateTo: () -> Unit,
    onGoogleLogInNavigateTo: () -> Unit,
    clickGoogleLogIn: () -> Job,
) {
    val logIn = LogInMaster(
        modifier = modifier,
        type = type,
        onEvent = onEvent,
        onBackNavigateTo = { onBackNavigateTo() },
        onLinkNavigateTo = onLinkNavigateTo,
        onLogInNavigateTo = onLogInNavigateTo,
        onGoogleLogInNavigateTo = onGoogleLogInNavigateTo,
        clickGoogleLogIn = clickGoogleLogIn,
    )

    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .clickable(
                interactionSource = MutableInteractionSource(),
                onClick = { focusManager.clearFocus() },
                indication = null
            ),
        topBar = { logIn.topAppBar() },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .consumeWindowInsets(innerPadding)
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(Dimensions.Spacing.large))
                logIn.flavourText()
                Spacer(modifier = Modifier.height(Dimensions.Spacing.large))
                logIn.textFields()
                CustomDivider(modifier = Modifier.padding(vertical = Dimensions.Spacing.large))
                logIn.logInActions()
            }
        }
    )
}
