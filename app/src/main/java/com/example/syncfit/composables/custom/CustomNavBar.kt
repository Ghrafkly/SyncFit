package com.example.syncfit.composables.custom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.ViewAgenda
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class NavItem {
    open val label: String = "Error"
    open val icon: @Composable () -> Unit = { Icon(imageVector = Icons.Outlined.Error, contentDescription = "Error") }
    open val selectedIcon: @Composable () -> Unit = { Icon(imageVector = Icons.Outlined.Error, contentDescription = "Error") }
    open val route: String = "error"

    object Timers : NavItem() {
        override val label: String = "Timers"
        override val icon: @Composable () -> Unit = { Icon(imageVector = Icons.Outlined.ViewAgenda, contentDescription = "Timers") }
        override val selectedIcon: @Composable () -> Unit = { Icon(imageVector = Icons.Filled.ViewAgenda, contentDescription = "Timers") }
        override val route: String = "timers_view"
    }

    object Create : NavItem() {
        override val label: String = "Add Timer"
        override val icon: @Composable () -> Unit = { Icon(imageVector = Icons.Outlined.AddCircleOutline, contentDescription = "Create Timer") }
        override val selectedIcon: @Composable () -> Unit = { Icon(imageVector = Icons.Filled.AddCircle, contentDescription = "Create Timer") }
        override val route = "timer_create"
    }

    object Profile : NavItem() {
        override val label: String = "Profile"
        override val icon: @Composable () -> Unit = { Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = "Profile") }
        override val selectedIcon: @Composable () -> Unit = { Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "Profile") }
        override val route = "profile_view"
    }

    object Settings : NavItem() {
        override val label: String = "Settings"
        override val icon: @Composable () -> Unit = { Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Settings") }
        override val selectedIcon: @Composable () -> Unit = { Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings") }
        override val route = "settings_view"
    }
}


@Composable
fun CustomNavBar(
    navController: NavController,
) {
    val backStackEntry = navController.currentBackStackEntryAsState()

    val items = listOf(
        NavItem.Timers,
        NavItem.Create,
        NavItem.Profile,
        NavItem.Settings,
    )

    NavigationBar {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route

            NavigationBarItem(
                icon = { if (selected) item.selectedIcon() else item.icon() },
                label = { Text(text = item.label) },
                selected = selected,
                onClick = { navController.navigate(item.route) {
                    launchSingleTop = true
                } },
            )
        }
    }
}
