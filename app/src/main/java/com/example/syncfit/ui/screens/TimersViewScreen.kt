package com.example.syncfit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.syncfit.SyncFitViewModel
import com.example.syncfit.composables.custom.CustomNavBar
import com.example.syncfit.composables.custom.MainTopAppBar
import com.example.syncfit.composables.screens.TimerCard
import com.example.syncfit.composables.screens.TimersViewActions
import com.example.syncfit.events.AppEvents
import com.example.syncfit.events.TimerEvents
import com.example.syncfit.ui.theme.Dimensions
import kotlin.math.abs

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TimersViewScreen(
    onEvent: (AppEvents) -> Unit,
    navController: NavController,
    viewModel: SyncFitViewModel,
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
                val data by viewModel.state.collectAsState()
                val timers = data.timerState.timers

                TimersViewActions(onCreateNavigateTo = { navController.navigate(ScreenConstants.Route.Timers.CREATE) })
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimensions.Spacing.small),
                ) {
                    for (timer in timers) {
                        item {
                            val dismissState = rememberDismissState(
                                confirmValueChange = {
                                    if (it == DismissValue.DismissedToStart) {
                                        onEvent(TimerEvents.DeleteTimer(timer))
                                    }
                                    true
                                }
                            )

                            SwipeToDismiss(
                                state = dismissState,
                                background = {
                                    val color = when (dismissState.dismissDirection) {
                                        DismissDirection.StartToEnd -> Color.Unspecified
                                        DismissDirection.EndToStart -> {
                                            val r = 1f
                                            var g = 1f - (abs(dismissState.requireOffset()) / 255f) * 0.5f
                                            var b = 1f - (abs(dismissState.requireOffset()) / 255f) * 0.5f

                                            if (g <= 0f) g = 0f
                                            if (b <= 0f) b = 0f

                                            Color(r, g, b)
                                        }
                                        else -> Color.Unspecified
                                    }

                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(color = color)
                                            .padding(10.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.White
                                        )
                                    }
                                },
                                dismissContent = {
                                    TimerCard(
                                        onEvent = onEvent,
                                        navController = navController,
                                        timer = timer,
                                    )
                                },
                                directions = setOf(DismissDirection.EndToStart)

                            )
                        }
                    }
                }
            }
        },
        bottomBar = { CustomNavBar(navController = navController) },
    )
}
