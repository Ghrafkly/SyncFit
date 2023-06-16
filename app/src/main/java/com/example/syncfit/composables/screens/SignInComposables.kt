package com.example.syncfit.composables.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.syncfit.SyncFitViewModel
import com.example.syncfit.composables.custom.CustomDivider
import com.example.syncfit.composables.custom.CustomGoogleButton
import com.example.syncfit.composables.custom.CustomOutlinedTextField
import com.example.syncfit.composables.custom.CustomTopAppBar
import com.example.syncfit.events.AppEvents
import com.example.syncfit.events.AuthEvents
import com.example.syncfit.states.AppState
import com.example.syncfit.ui.screens.ScreenConstants
import com.example.syncfit.ui.theme.Dimensions

@Composable
fun SignInTopAppBar(
    onBackNavigateTo: () -> Unit,
) {
    CustomTopAppBar(
        title = "Sign In",
        onBackNavigateTo = { onBackNavigateTo() },
    )
}

@Composable
fun SignInFlavourText(
    onLinkNavigateTo: () -> Unit
) {
    val text = "Don't have an account?"
    val link = "Create one here!"
    val linkedText = createLinkedText(text, link)

    ClickableText(
        text = linkedText,
        onClick = { onLinkNavigateTo() },
        style = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp
        )
    )
}

@Composable
fun SignInTextFields(
    state: AppState,
    onEvent: (AppEvents) -> Unit,
    navController: NavController,
    clickGoogleLogIn: () -> Unit,
) {
    onEvent(AuthEvents.ResetSignIn)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var invalidUser by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = state.userState.signInError) {
        Log.d("Custom", "SignInScreen: ${state.userState.signInError.isNullOrBlank()}")
        if (!state.userState.signInError.isNullOrBlank()) {
            invalidUser = true
        }
    }

    LaunchedEffect(key1 = state.userState.isSignInSuccessful) {
        if (state.userState.isSignInSuccessful) { navController.navigate(ScreenConstants.Route.Timers.HOME) }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimensions.Spacing.medium),
    ) {
        Box(modifier = Modifier.padding(bottom = 5.dp)) {
            CustomOutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    invalidUser = false
                    onEvent(AuthEvents.ResetSignIn)
                },
                label = { Text(text = "Email") },
                modifier = Modifier.fillMaxWidth(0.9f),
                trailingIcon = {
                    IconButton(onClick = {
                        email = ""
                        invalidUser = false
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                )
            )
        }
        Box(modifier = Modifier.padding(bottom = 5.dp)) {
            CustomOutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    invalidUser = false
                    onEvent(AuthEvents.ResetSignIn)
                },
                label = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth(0.9f),
                trailingIcon = {
                    IconButton(onClick = {
                        password = ""
                        invalidUser = false
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                )
            )
            if (invalidUser) {
                Text(
                    modifier = Modifier
                        .padding(top = 45.dp)
                        .align(Alignment.BottomStart)
                        .offset { IntOffset(0, 40) },
                    text = "Email or Password are incorrect",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    ),
                )
            }
        }

        CustomDivider()

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier.width(Dimensions.ButtonWidth.large),
                onClick = {
                    onEvent(AuthEvents.LocalSignIn(email, password))
                },
                content = { Text("Sign In") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
            CustomGoogleButton(
                modifier = Modifier.width(Dimensions.ButtonWidth.large),
                onClick = { clickGoogleLogIn() },
                text = "Sign up with Google"
            )
        }
    }
}
