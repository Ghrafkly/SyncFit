package com.example.syncfit

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.syncfit.authentication.GoogleAuthClient
import com.example.syncfit.database.daos.TimerDao
import com.example.syncfit.database.daos.UserDao
import com.example.syncfit.database.entities.Timer
import com.example.syncfit.database.entities.User
import com.example.syncfit.database.entities.UserWithTimers
import com.example.syncfit.events.AppEvents
import com.example.syncfit.events.AuthEvents
import com.example.syncfit.events.ClickEvents
import com.example.syncfit.events.TimerEvents
import com.example.syncfit.events.UserEvents
import com.example.syncfit.states.AppState
import com.example.syncfit.states.TimerState
import com.example.syncfit.states.UserState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sign

class SyncFitViewModel(
    private val userDao: UserDao,
    private val timerDao: TimerDao,
    private val googleAuthClient: GoogleAuthClient,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _userState = MutableStateFlow(UserState())
    private val _timerState = MutableStateFlow(TimerState())
    private val _state = MutableStateFlow(AppState())

    val state = combine(_state, _userState, _timerState,) { state, userState, timerState ->
        state.copy(
            userState = userState,
            timerState = timerState,
        )
    }.stateIn(viewModelScope,  SharingStarted.WhileSubscribed(5000), AppState())

    fun onEvent(event: AppEvents) {
        if (event is AuthEvents) authEvent(event)
        if (event is UserEvents) userEvent(event)
        if (event is TimerEvents) timerEvent(event)
    }

    private fun createAccount(user: User) {
        viewModelScope.launch {
            val dbUser: User? = userDao.getUserByKey(user.email)
            if (dbUser == null) {
                userDao.upsertUser(user)
                logIn(user)
            } else {
                _userState.update {
                    it.copy(
                        user = user,
                        isSignInSuccessful = false,
                        signInError = "User already exists",
                    )
                }
                Log.i("SyncFitViewModel", state.value.userState.toString())
            }
        }
    }

    private fun logIn(user: User) {
        _userState.update {
            it.copy(
                user = user,
                isSignInSuccessful = true,
                signInError = null,
            )
        }

        viewModelScope.launch {
            userDao.upsertUser(user)
        }
    }

    private fun userValidation(email: String, password: String) {
        viewModelScope.launch {
            val dbUser: User? = userDao.getUserByKey(email)
            Log.i("SyncFitViewModel", "dbUser: $dbUser")
            if (dbUser == null) {
                _userState.update {
                    it.copy(
                        user = User(),
                        isSignInSuccessful = false,
                        signInError = "Email or Password are incorrect",
                    )
                }
            } else {
                if (dbUser.email != email || dbUser.password != password) {
                    _userState.update {
                        it.copy(
                            user = User(),
                            isSignInSuccessful = false,
                            signInError = "Email or Password are incorrect",
                        )
                    }
                } else {
                    logIn(dbUser)
                }
            }
            Log.i("SyncFitViewModel", "Validation ${state.value.userState}")
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            if (state.value.userState.user.googleUser) {
                googleAuthClient.signOut()
            }
            resetUserState()
        }
    }

    private fun deleteAccount() {
        val user = state.value.userState.user
        Log.i("SyncFitViewModel", "Deleting user: $user")
        viewModelScope.launch {
            println("Deleting user: $user")
            userDao.deleteUser(user)
            signOut()
        }
    }

    private fun authEvent(event: AuthEvents) {
        when (event) {
            is AuthEvents.GoogleSignIn -> logIn(event.user)
            is AuthEvents.LocalSignIn -> userValidation(event.email, event.password)
            is AuthEvents.CreateAccount -> createAccount(event.user)
            is AuthEvents.LogOut -> signOut()
            AuthEvents.ResetSignIn -> println("activated")
        }
    }

    private fun userEvent(event: UserEvents) {
        when (event) {
            is UserEvents.CreateUser -> TODO()
            UserEvents.DeleteUser -> deleteAccount()
            is UserEvents.GetUserByKey -> TODO()
            is UserEvents.UpdateUser -> TODO()
        }
//        when (event) {
//            is UserEvents.CreateUser -> viewModelScope.launch { userDao.createUser(event.user) }
//            is UserEvents.DeleteUser -> viewModelScope.launch {
//                userDao.deleteUser(event.user)
//                resetUserState()
//            }
//            is UserEvents.UpdateUser -> viewModelScope.launch { userDao.updateUser(event.user) }
//            is UserEvents.GetUserByKey -> viewModelScope.launch {
//                userDao.getUserByKey(event.key).collect { user ->
//                    if (user != null) _userState.update { it.copy(user = user) }
//                }
//            }
//        }
    }

    private fun timerEvent(event: TimerEvents) {
//        when (event) {
//            is TimerEvents.CreateTimer -> viewModelScope.launch { timerDao.createTimer(event.timer) }
//            is TimerEvents.DeleteTimer -> viewModelScope.launch {
//                timerDao.deleteTimer(event.timer)
//                resetTimerState()
//            }
//            is TimerEvents.UpdateTimer -> viewModelScope.launch { timerDao.updateTimer(event.timer) }
//            is TimerEvents.GetTimerByKey -> viewModelScope.launch {
//                timerDao.getTimerByKey(event.timer.timerId).collect { timer ->
//                    if (timer != null) _timerState.update { it.copy(timer = timer) }
//                }
//            }
//            is TimerEvents.GetTimersByUser -> viewModelScope.launch {
//                timerDao.getTimersByUser(event.user.email).collect { timers ->
//                    if (timers.isNotEmpty()) _timerState.update { it.copy(timers = timers) }
//                }
//            }
//        }
    }

    private fun resetUserState() {
        _userState.update { it.copy(
            user = User(),
            isSignInSuccessful = false,
            signInError = null,
        ) }

        resetTimerState()
    }

    private fun resetTimerState() {
        _timerState.update { it.copy(
            timer = Timer(),
            timers = emptyList(),
            isTimerAdded = false,
            isTimerUpdated = false,
            isTimerDeleted = false,
            timerError = null,
        ) }
    }
}
