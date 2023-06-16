package com.example.syncfit.composables.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.syncfit.events.AppEvents
import com.example.syncfit.events.ClickEvents
import com.example.syncfit.ui.screens.ScreenConstants
import com.example.syncfit.ui.theme.Dimensions.*

@Composable
fun StartActions(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val buttonModifier = Modifier
        .width(ButtonWidth.large)
        .height(ButtonHeight.large)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
//            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = buttonModifier,
            onClick = { navController.navigate(ScreenConstants.Route.SignIn.JOIN) },
        ) {
            Text("Join Now", fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }
        Spacer(modifier = Modifier.padding(Spacing.medium))
        FilledTonalButton(
            modifier = buttonModifier,
            onClick = { navController.navigate(ScreenConstants.Route.SignIn.SIGN_IN) },
        ) {
            Text("Sign In", fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }
    }
}
