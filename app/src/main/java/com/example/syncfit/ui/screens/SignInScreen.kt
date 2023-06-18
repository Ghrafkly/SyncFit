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
import androidx.navigation.NavController
import com.example.syncfit.composables.screens.SignInFlavourText
import com.example.syncfit.composables.screens.SignInTextFields
import com.example.syncfit.composables.screens.SignInTopAppBar
import com.example.syncfit.events.AppEvents
import com.example.syncfit.states.AppState
import com.example.syncfit.ui.theme.Dimensions

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignInScreen(
    state: AppState,
    onEvent: (AppEvents) -> Unit,
    navController: NavController,
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
        topBar = { SignInTopAppBar(onBackNavigateTo = {
            navController.navigate(ScreenConstants.Route.START) {
                popUpTo("open") { saveState = true }
                launchSingleTop = true
            }
        }) },
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
                SignInFlavourText(onLinkNavigateTo = { navController.navigate(ScreenConstants.Route.SignIn.JOIN) })
                Spacer(modifier = Modifier.height(Dimensions.Spacing.large))
                SignInTextFields(
                    state = state,
                    onEvent = onEvent,
                    navController = navController,
                    clickGoogleLogIn = clickGoogleLogIn,
                )
            }
        }
    )
}
