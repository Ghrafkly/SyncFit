package com.example.syncfit.composables.custom

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.syncfit.events.AppEvents
import com.example.syncfit.ui.theme.Dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackNavigateTo: () -> Unit,
    iconButtons: @Composable () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = Dimensions.Spacing.medium),
        title = {
            Text(
                text = title,
                modifier = Modifier.padding(start = Dimensions.Spacing.medium),
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackNavigateTo() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
        actions = { iconButtons() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    iconButtons: @Composable () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = Dimensions.Spacing.medium),
        title = {
                Text(
                text = title,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = Dimensions.Spacing.medium)
            )
        },
        navigationIcon = { LogoNoText(modifier = Modifier.size(50.dp)) },
        actions = { iconButtons() }
    )
}
