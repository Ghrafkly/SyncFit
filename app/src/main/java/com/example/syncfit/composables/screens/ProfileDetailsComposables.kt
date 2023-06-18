package com.example.syncfit.composables.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import com.example.syncfit.composables.custom.CustomDivider
import com.example.syncfit.composables.custom.CustomOutlinedTextField
import com.example.syncfit.composables.custom.MainTopAppBar
import com.example.syncfit.database.entities.User
import com.example.syncfit.events.AppEvents
import com.example.syncfit.events.AuthEvents
import com.example.syncfit.events.UserEvents
import com.example.syncfit.states.AppState
import com.example.syncfit.ui.theme.Dimensions

@Composable
fun ProfileDetailsFields(
    state: AppState,
    onEvent: (AppEvents) -> Unit,
) {
    var fieldEditable by remember { mutableStateOf(false) }

    var firstName by remember { mutableStateOf(state.userState.user.firstname) }
    var lastName by remember { mutableStateOf(state.userState.user.lastname) }
    var phoneNumber by remember { mutableStateOf(state.userState.user.phoneNumber) }
    var password by remember { mutableStateOf(state.userState.user.password) }
    var confirmPassword by remember { mutableStateOf(state.userState.user.password) }

    var phoneNumberValidation by remember { mutableStateOf(false) }
    var passwordValidation by remember { mutableStateOf(false) }
    var confirmPasswordValidation by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimensions.Spacing.medium),
    ) {
        Box(modifier = Modifier.padding(bottom = 5.dp)) {
            CustomOutlinedTextField(
                enabled = false,
                value = state.userState.user.email,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(0.9f),
            )
        }
        Box(modifier = Modifier.padding(bottom = 5.dp)) {
            CustomOutlinedTextField(
                enabled = fieldEditable,
                value = firstName ?: "",
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
                enabled = fieldEditable,
                value = lastName ?: "",
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
                enabled = fieldEditable,
                value = phoneNumber ?: "",
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
                enabled = fieldEditable && !state.userState.user.googleUser,
                value = password ?: "",
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
                enabled = fieldEditable && !state.userState.user.googleUser,
                value = confirmPassword ?: "",
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.Spacing.medium),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { fieldEditable = true },
                modifier = Modifier
                    .width(Dimensions.ButtonWidth.small),
            ) {
                Text(text = "Edit")
            }
            FilledTonalButton(
                onClick = {
                    if (!state.userState.user.googleUser) {
                        passwordValidation = (password?.length ?: 0) < 8
                        confirmPasswordValidation = password != confirmPassword
                        phoneNumberValidation = (phoneNumber?.length ?: 0) < 9
                    }

                    if (phoneNumberValidation ||
                        passwordValidation ||
                        confirmPasswordValidation
                    ) return@FilledTonalButton

                    fieldEditable = false
                    onEvent(UserEvents.UpdateUser(
                        User(
                            email = state.userState.user.email,
                            firstname = firstName ?: "",
                            lastname = lastName ?: "",
                            phoneNumber = phoneNumber ?: "",
                            password = password ?: "",
                            googleUser = state.userState.user.googleUser,
                        )
                    ))
                },
                modifier = Modifier
                    .width(Dimensions.ButtonWidth.small),
            ) {
                Text(text = "Save")
            }
        }
    }
}
