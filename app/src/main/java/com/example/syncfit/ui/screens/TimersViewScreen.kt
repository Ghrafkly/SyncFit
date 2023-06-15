package com.example.syncfit.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.syncfit.composables.custom.CustomNavBar
import com.example.syncfit.composables.custom.MainTopAppBar
import com.example.syncfit.composables.screens.TimersViewActions
import com.example.syncfit.events.AppEvents
import com.example.syncfit.ui.theme.Dimensions

@Composable
fun TimerCard(
    modifier: Modifier = Modifier,
    onEvent: (AppEvents) -> Unit,
    onItemNavigateTo: () -> Unit,
    onPlayNavigateTo: () -> Unit
) {
    var favourite by remember { mutableStateOf(false) }

    ListItem(
        modifier = Modifier.clickable { onItemNavigateTo() },
        headlineContent = { Text("Leg Squats") },
        supportingContent = {
            Text("1m45s - 3 sets - 10 reps")
        },
        leadingContent = {
            IconButton(onClick = { favourite = !favourite }) {
                Icon(
                    imageVector = when (favourite) {
                        true -> Icons.Filled.Favorite
                        false -> Icons.Outlined.FavoriteBorder
                    },
                    contentDescription = "Favourite",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        trailingContent = {
            IconButton(onClick = { onPlayNavigateTo() }) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Start",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            }
        },
    )
    Divider()
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TimersViewScreen(
    modifier: Modifier = Modifier,
    onEvent: (AppEvents) -> Unit,
    navController: NavController,
    onCreateNavigateTo: () -> Unit,
    onItemNavigateTo: () -> Unit,
    onPlayNavigateTo: () -> Unit
) {
    Scaffold(
        topBar = {
            MainTopAppBar(
                title = "Timers",
                iconButtons = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .consumeWindowInsets(innerPadding)
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(top = Dimensions.Spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TimersViewActions(
                    onEvent = onEvent,
                    onCreateNavigateTo = onCreateNavigateTo,
                )
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimensions.Spacing.small),
                ) {
                    for (i in 0..15) {
                        item {
                            TimerCard(
                                onEvent = onEvent,
                                onItemNavigateTo = onItemNavigateTo,
                                onPlayNavigateTo = onPlayNavigateTo
                            )
                        }
                    }
                }
            }
        },
        bottomBar = { CustomNavBar(navController = navController) },
    )
}
