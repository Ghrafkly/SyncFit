package com.example.syncfit.ui.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.example.syncfit.composables.screens.CreateAccountFlavourText
import com.example.syncfit.composables.screens.CreateAccountTextFields
import com.example.syncfit.composables.screens.CreateAccountTopAppBar
import com.example.syncfit.composables.screens.SignInFlavourText
import com.example.syncfit.composables.screens.SignInTextFields
import com.example.syncfit.composables.screens.SignInTopAppBar
import com.example.syncfit.events.AppEvents
import com.example.syncfit.states.AppState
import com.example.syncfit.ui.theme.Dimensions

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateAccountScreen(
    state: AppState,
    onEvent: (AppEvents) -> Unit,
    onBackNavigateTo: () -> Unit,
    onLinkNavigateTo: () -> Unit,
    onLogInNavigateTo: () -> Unit,
    onGoogleLogInNavigateTo: () -> Unit,
    clickGoogleLogIn: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .clickable(
                interactionSource = MutableInteractionSource(),
                onClick = { focusManager.clearFocus() },
                indication = null
            ),
        topBar = { CreateAccountTopAppBar(onBackNavigateTo = { onBackNavigateTo() }) },
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
                CreateAccountFlavourText(onLinkNavigateTo = { onLinkNavigateTo() })
                Spacer(modifier = Modifier.height(Dimensions.Spacing.large))
                CreateAccountTextFields(
                    state = state,
                    onEvent = onEvent,
                    onLogInNavigateTo = { onLogInNavigateTo() },
                    onGoogleLogInNavigateTo = { onGoogleLogInNavigateTo() },
                    clickGoogleLogIn = clickGoogleLogIn,
                )
            }
        }
    )
}
