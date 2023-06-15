package com.example.syncfit.composables.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.syncfit.composables.custom.CustomDivider
import com.example.syncfit.composables.custom.CustomGoogleButton
import com.example.syncfit.composables.custom.CustomOutlinedTextField
import com.example.syncfit.composables.custom.CustomTopAppBar
import com.example.syncfit.events.AppEvents
import com.example.syncfit.ui.theme.Dimensions
import kotlinx.coroutines.Job

data class TypeData(
    val type: String,
    val title: String,
    val flavourTextText: String,
    val flavourTextLink: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String,
    val phoneNumber: String? = null,
    val password: String,
    val confirmPassword: String? = null,
    val logIn: String,
    val google: String
)

class LogInMaster(
    modifier: Modifier = Modifier,
    type: String,
    onEvent: (AppEvents) -> Unit,
    onBackNavigateTo: () -> Unit,
    onLinkNavigateTo: () -> Unit,
    onLogInNavigateTo: () -> Unit,
    onGoogleLogInNavigateTo: () -> Unit,
    clickGoogleLogIn: () -> Job?,
) {
    constructor(
        modifier: Modifier = Modifier,
        type: String,
        onEvent: (AppEvents) -> Unit,
    ) : this(
        modifier = modifier,
        type = type,
        onEvent = onEvent,
        onBackNavigateTo = {},
        onLinkNavigateTo = {},
        onLogInNavigateTo = {},
        onGoogleLogInNavigateTo = {},
        clickGoogleLogIn = { null },
    )

    val data = TypeData(
        type = type,
        title = if (type == "sign_in") "Sign in" else if (type == "profile_details") "Profile Details" else "Create Account",
        flavourTextText = if (type == "sign_in") "Don't have an account?" else "Already have an account?",
        flavourTextLink = if (type == "sign_in") "Create one here!" else "Sign in here!",
        firstName = if (type == "sign_in") null else "First Name",
        lastName = if (type == "sign_in") null else "Last Name",
        email = "Email",
        phoneNumber = if (type == "sign_in") null else "Phone Number",
        password = "Password",
        confirmPassword = if (type == "sign_in") null else "Confirm Password",
        logIn = if (type == "sign_in") "Sign in" else "Create Account",
        google = if (type == "sign_in") "Sign in with Google" else "Sign up with Google"
    )

    val topAppBar: @Composable () -> Unit = {
        LogInTopAppBar(
            modifier = modifier,
            title = data.title,
            onBackNavigateTo = onBackNavigateTo
        )
    }

    val flavourText: @Composable () -> Unit = {
        LogInFlavourText(
            modifier = modifier,
            text = data.flavourTextText,
            link = data.flavourTextLink,
            onLinkNavigateTo = onLinkNavigateTo
        )
    }

    val logInActions: @Composable () -> Unit = {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogInActions(
                modifier = modifier,
                google = data.google,
                logIn = data.logIn,
                onLogInNavigateTo = onLogInNavigateTo,
                onGoogleLogInNavigateTo = onGoogleLogInNavigateTo,
                clickGoogleLogIn = clickGoogleLogIn as () -> Job,
            )
        }
    }

    val textFields: @Composable () -> Unit = {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimensions.Spacing.medium),
        ) {
            val fields = textFields(data)
            fields.values.removeIf() { it == "" }

            fields.onEachIndexed() { index, (key, value) ->
                LoginTextField(
                    modifier = modifier,
                    key = key,
                    label = value,
                    lastElement = index == fields.size - 1
                )
            }
        }
    }

    private fun textFields(data: TypeData): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map["firstName"] = data.firstName ?: ""
        map["lastName"] = data.lastName ?: ""
        map["email"] = data.email
        map["phoneNumber"] = data.phoneNumber ?: ""
        map["password"] = data.password
        map["confirmPassword"] = data.confirmPassword ?: ""
        return map
    }
}


@Composable
fun LogInTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackNavigateTo: () -> Unit
) {
    CustomTopAppBar(
        title = title,
        onBackNavigateTo = onBackNavigateTo,
    )
}

@Composable
fun LogInFlavourText(
    modifier: Modifier = Modifier,
    text: String,
    link: String,
    onLinkNavigateTo: () -> Unit
) {
    val linkedText = buildAnnotatedString {
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
fun LoginTextField(
    modifier: Modifier = Modifier,
    key: String,
    label: String,
    lastElement: Boolean = false,
) {
    var userInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    CustomOutlinedTextField(
        modifier = modifier.fillMaxWidth(0.9f),
        value = userInput,
        onValueChange = { userInput = it },
        label = { Text(label) },
        trailingIcon = {
            IconButton(onClick = { userInput = "" }) {
                Icon(Icons.Default.Close, contentDescription = "Clear")
            }
        },
        visualTransformation = when {
            Regex("[pP]assword").containsMatchIn(key) -> PasswordVisualTransformation()
            else -> VisualTransformation.None
        },
        keyboardOptions = when (key) {
            "phoneNumber" -> KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )

            "email" -> KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )

            else -> when {
                lastElement -> KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )

                else -> KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            }
        },
        keyboardActions = KeyboardActions(
            onNext = {
                when {
                    lastElement -> focusManager.clearFocus()
                    else -> focusManager.moveFocus(FocusDirection.Down)
                }

            }
        ),
    )
    if (key == "phoneNumber") CustomDivider()
}

@Composable
fun LogInActions(
    modifier: Modifier = Modifier,
    google: String,
    logIn: String,
    onLogInNavigateTo: () -> Unit,
    onGoogleLogInNavigateTo: () -> Unit,
    clickGoogleLogIn: () -> Job
) {
    Button(
        modifier = modifier.width(Dimensions.ButtonWidth.large),
        onClick = onLogInNavigateTo,
        content = { Text(logIn) },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    )
    CustomGoogleButton(
        modifier = Modifier.width(Dimensions.ButtonWidth.large),
        onClick = {
            clickGoogleLogIn()
        },
        text = google
    )
}
