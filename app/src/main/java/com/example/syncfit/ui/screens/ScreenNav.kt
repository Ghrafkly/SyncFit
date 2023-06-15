package com.example.syncfit.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.syncfit.events.AppEvents
import com.example.syncfit.states.AppState
import kotlinx.coroutines.Job

data class Nav(var route: String, var popUpTo: String? = null)

sealed interface ScreenNav {
    data class Start(
        val onEvent: (AppEvents) -> Unit,
        val onJoinNavigateTo: () -> Unit,
        val onSignInNavigateTo: () -> Unit,
    ) {
        companion object {
            val nav: Nav = Nav("start")
        }

        @Composable
        fun Render() {
            StartScreen(
                onEvent = onEvent,
                onJoinNavigateTo = onJoinNavigateTo,
                onSignInNavigateTo = onSignInNavigateTo,
            )
        }
    }

    data class SignIn(
        val state: AppState,
        val onEvent: (AppEvents) -> Unit,
        val onBackNavigateTo: () -> Unit,
        val onLinkNavigateTo: () -> Unit,
        val onLogInNavigateTo: () -> Unit,
        val onGoogleLogInNavigateTo: () -> Unit,
        val clickGoogleLogIn: () -> Unit,
    ) {
        companion object {
            val nav: Nav = Nav("sign_in", )
        }

        @Composable
        fun Render() {
            SignInScreen(
                state = state,
                onEvent = onEvent,
                onBackNavigateTo = onBackNavigateTo,
                onLinkNavigateTo = onLinkNavigateTo,
                onLogInNavigateTo = onLogInNavigateTo,
                onGoogleLogInNavigateTo = onGoogleLogInNavigateTo,
                clickGoogleLogIn = clickGoogleLogIn,
            )
        }
    }

    data class Join(
        val state: AppState,
        val onEvent: (AppEvents) -> Unit,
        val onBackNavigateTo: () -> Unit,
        val onLinkNavigateTo: () -> Unit,
        val onLogInNavigateTo: () -> Unit,
        val onGoogleLogInNavigateTo: () -> Unit,
        val clickGoogleLogIn: () -> Unit,
    ) {
        companion object {
            val nav: Nav = Nav("join", )
        }

        @Composable
        fun Render() {
            CreateAccountScreen(
                state = state,
                onEvent = onEvent,
                onBackNavigateTo = onBackNavigateTo,
                onLinkNavigateTo = onLinkNavigateTo,
                onLogInNavigateTo = onLogInNavigateTo,
                onGoogleLogInNavigateTo = onGoogleLogInNavigateTo,
                clickGoogleLogIn = clickGoogleLogIn,
            )
        }
    }


//    data class LogIn(
//        val type: String,
//        val onEvent: (AppEvents) -> Unit,
//        val onBackNavigateTo: () -> Unit,
//        val onLinkNavigateTo: () -> Unit,
//        val onLogInNavigateTo: () -> Unit,
//        val onGoogleLogInNavigateTo: () -> Unit,
//        val clickGoogleLogIn: () -> Job,
//    ) {
//        @Composable
//        fun Render() {
//            LogInScreen(
//                type = type,
//                onEvent = onEvent,
//                onBackNavigateTo = onBackNavigateTo,
//                onLinkNavigateTo = onLinkNavigateTo,
//                onLogInNavigateTo = onLogInNavigateTo,
//                onGoogleLogInNavigateTo = onGoogleLogInNavigateTo,
//                clickGoogleLogIn = clickGoogleLogIn,
//            )
//        }
//    }

    data class Home(
        val event: (AppEvents) -> Unit,
        val navController: NavController,
        val onCreateNavigateTo: () -> Unit,
        val onItemNavigateTo: () -> Unit,
        val onPlayNavigateTo: () -> Unit
    ) {
        companion object {
            val nav: Nav = Nav("timers_view")
        }

        @Composable
        fun Render() {
            TimersViewScreen(
                onEvent = event,
                navController = navController,
                onCreateNavigateTo = onCreateNavigateTo,
                onItemNavigateTo = onItemNavigateTo,
                onPlayNavigateTo = onPlayNavigateTo
            )
        }
    }

    data class TimerCreate(
        val event: (AppEvents) -> Unit,
        val navController: NavController
    ) {
        companion object {
            val nav: Nav = Nav("timer_create")
        }

        @Composable
        fun Render() {
            TimerCreateScreen(
                onEvent = event,
                navController = navController
            )
        }
    }

    data class TimerDetails(
        val event: (AppEvents) -> Unit,
        val navController: NavController,
        val onPlayNavigateTo: () -> Unit,
        val onDeleteTimerNavigateTo: () -> Unit
    ) {
        companion object {
            val nav: Nav = Nav("timer_details")
        }

        @Composable
        fun Render() {
            TimerDetailsScreen(
                onEvent = event,
                navController = navController,
                onPlayNavigateTo = onPlayNavigateTo,
                onDeleteTimerNavigateTo = onDeleteTimerNavigateTo,
            )
        }
    }

    data class TimerRun(
        val event: (AppEvents) -> Unit,
        val navController: NavController,
    ) {
        companion object {
            val nav: Nav = Nav("timer_run")
        }

        @Composable
        fun Render() {
            TimerRunScreen(
                onEvent = event,
                navController = navController,
            )
        }
    }

    data class Profile(
        val event: (AppEvents) -> Unit,
        val navController: NavController,
        val onEditNavigateTo: () -> Unit,
        val onLogOutNavigateTo: () -> Unit,
        val clickGoogleSignOut: () -> Job,
    ) {
        companion object {
            val nav: Nav = Nav("profile_view")
        }

        @Composable
        fun Render() {
            ProfileScreen(
                onEvent = event,
                navController = navController,
                onEditNavigateTo = onEditNavigateTo,
                onLogOutNavigateTo = onLogOutNavigateTo,
                clickGoogleSignOut = clickGoogleSignOut,
            )
        }
    }

    data class ProfileDetails(
        val event: (AppEvents) -> Unit,
        val navController: NavController,
    ) {
        companion object {
            val nav: Nav = Nav("profile_details")
        }

        @Composable
        fun Render() {
            ProfileDetailsScreen(
                onEvent = event,
                navController = navController,
            )
        }
    }

    data class Settings(
        val event: (AppEvents) -> Unit,
        val navController: NavController,
        val onDeleteAccountNavigateTo: () -> Unit,
        val clickDeleteAccount: () -> Unit,
    ) {
        companion object {
            val nav: Nav = Nav("settings_view")
        }

        @Composable
        fun Render() {
            SettingsScreen(
                onEvent = event,
                navController = navController,
                onDeleteAccountNavigateTo = onDeleteAccountNavigateTo,
                clickDeleteAccount = clickDeleteAccount
            )
        }
    }
}
