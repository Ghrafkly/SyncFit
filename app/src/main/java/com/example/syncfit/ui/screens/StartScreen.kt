package com.example.syncfit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.syncfit.composables.custom.LogoWithText
import com.example.syncfit.composables.screens.StartActions
import com.example.syncfit.events.AppEvents

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onEvent: (AppEvents) -> Unit,
    onJoinNavigateTo: () -> Unit,
    onSignInNavigateTo: () -> Unit,
) {
    Scaffold(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(innerPadding)
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            LogoWithText()
            StartActions(
                onJoinNavigateTo = onJoinNavigateTo,
                onSignInNavigateTo = onSignInNavigateTo,
            )
        }
    }
}
