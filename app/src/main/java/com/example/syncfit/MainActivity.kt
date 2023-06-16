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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
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
import com.example.syncfit.ui.screens.CreateAccountScreen
import com.example.syncfit.ui.screens.ProfileDetailsScreen
import com.example.syncfit.ui.screens.ProfileScreen
import com.example.syncfit.ui.screens.ScreenConstants
import com.example.syncfit.ui.screens.SettingsScreen
import com.example.syncfit.ui.screens.SignInScreen
import com.example.syncfit.ui.screens.StartScreen
import com.example.syncfit.ui.screens.TimerCreateScreen
import com.example.syncfit.ui.screens.TimerDetailsScreen
import com.example.syncfit.ui.screens.TimerRunScreen
import com.example.syncfit.ui.screens.TimersViewScreen
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
                    return SyncFitViewModel(db.userDao, db.timerDao, googleAuthClient, SavedStateHandle()) as T
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

                        navController.navigate(ScreenConstants.Route.Timers.HOME)
                    }
                }

                LaunchedEffect(key1 = state.userState.signInError) {
                    if (state.userState.signInError != null) {
                        Log.d("MainActivity", state.userState.signInError!!)
                        Toast.makeText(
                            applicationContext,
                            state.userState.signInError,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                LaunchedEffect(key1 = Unit) {
                    if (googleAuthClient.getSignedInGoogleUser() != null) {
                        Log.i("MainActivity", "User already signed in")
                        navController.navigate(ScreenConstants.Route.Timers.HOME)
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
                    navigation(
                        route = "open",
                        startDestination = ScreenConstants.Route.START
                    ) {
                        composable(ScreenConstants.Route.START) {
                            StartScreen(
                                onEvent = viewModel::onEvent,
                                navController = navController,
                            )
                        }
                        composable(ScreenConstants.Route.SignIn.SIGN_IN) {
                            SignInScreen(
                                state = state,
                                onEvent = viewModel::onEvent,
                                navController = navController,
                                clickGoogleLogIn = { clickGoogleLogIn() }
                            )
                        }
                        composable(ScreenConstants.Route.SignIn.JOIN) {
                            CreateAccountScreen(
                                state = state,
                                onEvent = viewModel::onEvent,
                                navController = navController,
                                clickGoogleLogIn = { clickGoogleLogIn() }
                            )
                        }
                    }
                    navigation(
                        route = "main",
                        startDestination = ScreenConstants.Route.Timers.HOME
                    ) {
                        composable(ScreenConstants.Route.Timers.HOME) {
                            TimersViewScreen(
                                state = state,
                                onEvent = viewModel::onEvent,
                                navController = navController,
                            )
                        }
                        composable(ScreenConstants.Route.Timers.CREATE) {
                            TimerCreateScreen(
                                state = state,
                                onEvent = viewModel::onEvent,
                                navController = navController,
                            )
                        }
                        composable(ScreenConstants.Route.Timers.DETAILS) {
                            TimerDetailsScreen(
                                state = state,
                                onEvent = viewModel::onEvent,
                                navController = navController,
                            )
                        }
                        composable(ScreenConstants.Route.Timers.RUN) {
                            TimerRunScreen(
                                state = state,
                                onEvent = viewModel::onEvent,
                                navController = navController,
                            )
                        }
                        composable(ScreenConstants.Route.Profile.HOME) {
                            ProfileScreen(
                                state = state,
                                onEvent = viewModel::onEvent,
                                navController = navController,
                                clickGoogleSignOut = { clickGoogleSignOut() }
                            )
                        }
                        composable(ScreenConstants.Route.Profile.EDIT) {
                            ProfileDetailsScreen(
                                state = state,
                                onEvent = viewModel::onEvent,
                                navController = navController,
                            )
                        }
                        composable(ScreenConstants.Route.SETTINGS) {
                            SettingsScreen(
                                state = state,
                                onEvent = viewModel::onEvent,
                                navController = navController,
                            )
                        }
                    }
                }
            }
        }
    }
}
