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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.syncfit.composables.custom.CustomDivider
import com.example.syncfit.composables.custom.CustomGoogleButton
import com.example.syncfit.composables.custom.CustomOutlinedTextField
import com.example.syncfit.composables.custom.CustomTopAppBar
import com.example.syncfit.database.entities.User
import com.example.syncfit.events.AppEvents
import com.example.syncfit.events.AuthEvents
import com.example.syncfit.states.AppState
import com.example.syncfit.ui.screens.ScreenConstants
import com.example.syncfit.ui.theme.Dimensions

@Composable
fun CreateAccountTopAppBar(
    onBackNavigateTo: () -> Unit,
) {
    CustomTopAppBar(
        title = "Create Account",
        onBackNavigateTo = { onBackNavigateTo() },
    )
}

@Composable
fun CreateAccountFlavourText(
    onLinkNavigateTo: () -> Unit
) {
    val text = "Already have an account?"
    val link = "Sign in here!"
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
fun CreateAccountTextFields(
    state: AppState,
    onEvent: (AppEvents) -> Unit,
    navController: NavController,
    clickGoogleLogIn: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var userExists by remember { mutableStateOf(false) }

    var emailValidation by remember { mutableStateOf(false) }
    var phoneNumberValidation by remember { mutableStateOf(false) }
    var passwordValidation by remember { mutableStateOf(false) }
    var confirmPasswordValidation by remember { mutableStateOf(false) }

    val emailRegex =
        Regex("^[A-Za-z]+[A-Za-z\\-.+_][A-Za-z]+@([A-Za-z][A-Za-z\\-]+\\.)+[A-Za-z\\-]{2,10}$")

    LaunchedEffect(key1 = state.userState.signInError) {
        Log.d("Custom", "SignInScreen: ${state.userState.signInError.isNullOrBlank()}")
        if (!state.userState.signInError.isNullOrBlank()) {
            userExists = true
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
                    emailValidation = false
                    userExists = false
                    onEvent(AuthEvents.ResetSignIn)
                },
                label = { Text(text = "Email") },
                modifier = Modifier.fillMaxWidth(0.9f),
                trailingIcon = {
                    IconButton(onClick = {
                        email = ""
                        emailValidation = false
                        userExists = false
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
                isError = emailValidation || userExists,
            )
            if (emailValidation) {
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset { IntOffset(0, 40) },
                    text = "Email must be valid",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                )
            } else if (userExists) {
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset { IntOffset(0, 40) },
                    text = "User already exists",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                )
            }
        }
        Box(modifier = Modifier.padding(bottom = 5.dp)) {
            CustomOutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text(text = "First Name") },
                modifier = Modifier.fillMaxWidth(0.9f),
                trailingIcon = {
                    IconButton(onClick = { firstName = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
            )
        }
        Box(modifier = Modifier.padding(bottom = 5.dp)) {
            CustomOutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(text = "Last Name") },
                modifier = Modifier.fillMaxWidth(0.9f),
                trailingIcon = {
                    IconButton(onClick = { lastName = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
            )
        }
        Box(modifier = Modifier.padding(bottom = 5.dp)) {
            CustomOutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it
                    phoneNumberValidation = false
                },
                label = { Text(text = "Phone Number") },
                modifier = Modifier.fillMaxWidth(0.9f),
                trailingIcon = {
                    IconButton(onClick = {
                        phoneNumber = ""
                        phoneNumberValidation = false
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next,
                ),
                isError = phoneNumberValidation
            )
            if (phoneNumberValidation) {
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset { IntOffset(0, 40) },
                    text = "Phone number must be 9 digits long",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                )
            }
        }

        CustomDivider()

        Box(modifier = Modifier.padding(bottom = 5.dp)) {
            CustomOutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordValidation = false
                },
                label = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth(0.9f),
                trailingIcon = {
                    IconButton(onClick = {
                        password = ""
                        passwordValidation = false
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next,
                ),
                isError = passwordValidation,
            )
            if (passwordValidation) {
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset { IntOffset(0, 40) },
                    text = "Password must be at least 8 characters long",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                )
            }
        }
        Box(modifier = Modifier.padding(bottom = 5.dp)) {
            CustomOutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordValidation = false
                },
                label = { Text(text = "Confirm Password") },
                modifier = Modifier.fillMaxWidth(0.9f),
                trailingIcon = {
                    IconButton(onClick = {
                        confirmPassword = ""
                        confirmPasswordValidation = false
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                isError = confirmPasswordValidation,
            )
            if (confirmPasswordValidation) {
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset { IntOffset(0, 40) },
                    text = "Passwords do not match",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
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
                    emailValidation = !emailRegex.matches(email)
                    phoneNumberValidation = phoneNumber.length < 9
                    passwordValidation = password.length < 8
                    confirmPasswordValidation = password != confirmPassword

                    Log.i("CreateAccount", "emailValidation: $emailValidation")
                    Log.i("CreateAccount", "phoneNumberValidation: $phoneNumberValidation")

                    if (!emailValidation &&
                        !phoneNumberValidation &&
                        !passwordValidation &&
                        !confirmPasswordValidation
                    ) {
                        onEvent(AuthEvents.CreateAccount(
                            User(
                                email = email,
                                firstname = firstName,
                                lastname = lastName,
                                phoneNumber = phoneNumber,
                                password = password,
                            )
                        ))
                    }
                },
                content = { Text("Create Account") },
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

@Composable
fun createLinkedText(text: String, link: String): AnnotatedString {
    return buildAnnotatedString {
        val mStr = "$text $link"
        val mStartIndex = mStr.indexOf(link)
        val mEndIndex = mStartIndex + (link).length

        append(mStr)
        addStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            ), start = mStartIndex, end = mEndIndex
        )
    }
}
