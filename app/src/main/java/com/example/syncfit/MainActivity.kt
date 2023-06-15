package com.example.syncfit

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.room.Room
import com.example.syncfit.authentication.GoogleAuthClient
import com.example.syncfit.database.SyncFitDatabase
import com.example.syncfit.events.AppEvents
import com.example.syncfit.events.AuthEvents
import com.example.syncfit.events.UserEvents
import com.example.syncfit.states.AppState
import com.example.syncfit.ui.screens.ScreenNav
import com.example.syncfit.ui.theme.SyncFitTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            SyncFitDatabase::class.java,
            "syncfit.db"
        ).build()
    }

    private val googleAuthClient by lazy {
        GoogleAuthClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext),
        )
    }

    private val viewModel by viewModels<SyncFitViewModel> (
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SyncFitViewModel(db.userDao, db.timerDao, googleAuthClient) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            SyncFitTheme {
                val state by viewModel.state.collectAsStateWithLifecycle()
                val navController = rememberNavController()

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            lifecycleScope.launch {
                                val signInResult = googleAuthClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )

                                signInResult.user?.let { viewModel.onEvent(AuthEvents.GoogleSignIn(it)) }
                            }
                        }
                    }
                )

                LaunchedEffect(key1 = state.userState.isSignInSuccessful) {
                    if (state.userState.isSignInSuccessful) {
                        Log.d("MainActivity", "Sign in successful")
                        Toast.makeText(
                            applicationContext,
                            "Sign in successful",
                            Toast.LENGTH_SHORT
                        ).show()

                        navController.navigate(ScreenNav.Home.nav.route)
                    }
                }

                LaunchedEffect(key1 = Unit) {
                    if (googleAuthClient.getSignedInGoogleUser() != null) {
                        Log.i("MainActivity", "User already signed in")
                        navController.navigate(ScreenNav.Home.nav.route)
                    }
                }

                val clickGoogleLogIn = {
                    lifecycleScope.launch {
                        val signInIntentSender = googleAuthClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }

                val clickGoogleSignOut = {
                    lifecycleScope.launch {
                        googleAuthClient.signOut()
                        viewModel.onEvent(AuthEvents.LogOut)
                        Log.d("MainActivity", "Sign Out")
                        Toast.makeText(
                            applicationContext,
                            "Signed out",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = "open"
                ) {
                    loginGraph(
                        navController = navController,
                        event = viewModel::onEvent,
                        state = state,
                        clickGoogleLogIn = clickGoogleLogIn,
                    )
                    mainGraph(
                        navController = navController,
                        onEvent = viewModel::onEvent,
                        state = state,
                        clickGoogleSignOut = clickGoogleSignOut
                    )
                }
            }
        }
    }
}

fun NavGraphBuilder.loginGraph(
    navController: NavController,
    event: (AppEvents) -> Unit,
    state: AppState,
    clickGoogleLogIn: () -> Job,
) {
    navigation(
        route = "open",
        startDestination = ScreenNav.Start.nav.route
    ) {
        composable(ScreenNav.Start.nav.route) {
            val onJoinNavigateTo = { navController.navigate(ScreenNav.Join.nav.route) }
            val onSignInNavigateTo = { navController.navigate(ScreenNav.SignIn.nav.route) }

            ScreenNav.Start(
                onEvent = event,
                onJoinNavigateTo = { onJoinNavigateTo() },
                onSignInNavigateTo = { onSignInNavigateTo() }
            ).Render()
        }
        composable(ScreenNav.SignIn.nav.route) {
            val onBackNavigateTo = {
                navController.navigate(ScreenNav.Start.nav.route) {
                    popUpTo("open") {
                        saveState = true
                    }
                    launchSingleTop = true
                }
            }
            val onLinkNavigateTo = { navController.navigate(ScreenNav.Join.nav.route) }
            val onLogInNavigateTo = { navController.navigate(ScreenNav.Home.nav.route) }
            val onGoogleLogInNavigateTo = { navController.navigate(ScreenNav.Home.nav.route) }

            ScreenNav.SignIn(
                state = state,
                onEvent = event,
                onBackNavigateTo = { onBackNavigateTo() },
                onLinkNavigateTo = { onLinkNavigateTo() },
                onLogInNavigateTo = { onLogInNavigateTo() },
                onGoogleLogInNavigateTo = { onGoogleLogInNavigateTo() },
                clickGoogleLogIn = { clickGoogleLogIn() }
            ).Render()
        }
        composable(ScreenNav.Join.nav.route) {
            val onBackNavigateTo = {
                navController.navigate("open") {
                    popUpTo("open") {
                        saveState = true
                    }
                    launchSingleTop = true
                }
            }
            val onLinkNavigateTo = { navController.navigate(ScreenNav.SignIn.nav.route) }
            val onLogInNavigateTo = { navController.navigate(ScreenNav.Home.nav.route) }
            val onGoogleLogInNavigateTo = { navController.navigate(ScreenNav.Home.nav.route) }

            ScreenNav.Join(
                state = state,
                onEvent = event,
                onBackNavigateTo = { onBackNavigateTo() },
                onLinkNavigateTo = { onLinkNavigateTo() },
                onLogInNavigateTo = { onLogInNavigateTo() },
                onGoogleLogInNavigateTo = { onGoogleLogInNavigateTo() },
                clickGoogleLogIn = { clickGoogleLogIn() }
            ).Render()
        }
    }
}

fun NavGraphBuilder.mainGraph(
    state: AppState,
    onEvent: (AppEvents) -> Unit,
    navController: NavController,
    clickGoogleSignOut : () -> Job,
) {
    navigation(
        route = "main",
        startDestination = ScreenNav.Home.nav.route
    ) {
        composable(ScreenNav.Home.nav.route) {
            val onCreateNavigateTo = { navController.navigate(ScreenNav.TimerCreate.nav.route) }
            val onItemNavigateTo = { navController.navigate(ScreenNav.TimerDetails.nav.route) }
            val onPlayNavigateTo = { navController.navigate(ScreenNav.TimerRun.nav.route) }

            ScreenNav.Home(
                event = onEvent,
                navController = navController,
                onCreateNavigateTo = { onCreateNavigateTo() },
                onItemNavigateTo = { onItemNavigateTo() },
                onPlayNavigateTo = { onPlayNavigateTo() }
            ).Render()
        }
        composable(ScreenNav.TimerCreate.nav.route) {
            ScreenNav.TimerCreate(
                event = onEvent,
                navController = navController,
            ).Render()
        }
        composable(ScreenNav.TimerDetails.nav.route) {
            val onPlayNavigateTo = { navController.navigate(ScreenNav.TimerRun.nav.route) }
            val onDeleteTimerNavigateTo = { navController.navigate(ScreenNav.Home.nav.route) }

            ScreenNav.TimerDetails(
                event = onEvent,
                navController = navController,
                onPlayNavigateTo = { onPlayNavigateTo() },
                onDeleteTimerNavigateTo = { onDeleteTimerNavigateTo() }
            ).Render()
        }
        composable(ScreenNav.TimerRun.nav.route) {
            ScreenNav.TimerRun(
                event = onEvent,
                navController = navController,
            ).Render()
        }

        composable(ScreenNav.Profile.nav.route) {
            val onEditNavigateTo = { navController.navigate(ScreenNav.ProfileDetails.nav.route) }
            val onLogOutNavigateTo = {
                navController.navigate(ScreenNav.Start.nav.route) {
                    popUpTo("open") {
                        inclusive = true
                    }
                }
            }

            ScreenNav.Profile(
                event = onEvent,
                navController = navController,
                onEditNavigateTo = { onEditNavigateTo() },
                onLogOutNavigateTo = { onLogOutNavigateTo() },
                clickGoogleSignOut = clickGoogleSignOut
            ).Render()
        }
        composable(ScreenNav.ProfileDetails.nav.route) {
            ScreenNav.ProfileDetails(
                event = onEvent,
                navController = navController,
            ).Render()
        }


        composable(ScreenNav.Settings.nav.route) {
            val onDeleteAccountNavigateTo = { navController.navigate(ScreenNav.Start.nav.route) }
            val clickDeleteAccount = { onEvent(UserEvents.DeleteUser) }

            ScreenNav.Settings(
                event = onEvent,
                navController = navController,
                onDeleteAccountNavigateTo = { onDeleteAccountNavigateTo() },
                clickDeleteAccount = { clickDeleteAccount() }
            ).Render()
        }
    }
}
