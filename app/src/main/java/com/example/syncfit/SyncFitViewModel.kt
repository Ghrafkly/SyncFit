package com.example.syncfit

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import com.example.syncfit.database.models.Environment
import com.example.syncfit.database.models.Intensity
import com.example.syncfit.database.models.Interval
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
    private val _timer = MutableStateFlow(Timer())
    private val _state = MutableStateFlow(AppState())

    val state = combine(_state, _userState, _timerState) { state, userState, timerState ->
        state.copy(
            userState = userState,
            timerState = timerState,
        )
    }.stateIn(viewModelScope,  SharingStarted.WhileSubscribed(5000), AppState())

    private fun updateTimerName(name: String) {
        _timer.update { it.copy(timerName = name) }
        _timerState.update { it.copy(isTimerNameValid = true) }
    }

    private fun updateTimerIntervals(intervals: List<Interval>) {
        _timer.update { it.copy(timerIntervals = intervals) }
        _timerState.update { it.copy(isTimerIntervalsValid = true) }
    }

    private fun updateTimerRepeats(repeats: Int) {
        _timer.update { it.copy(timerRepeats = repeats) }
        _timerState.update { it.copy(isTimerRepeatsValid = true) }
    }

    private fun updateTimerIntensity(intensity: Intensity) {
        _timer.update { it.copy(timerIntensity = intensity) }
        _timerState.update { it.copy(isTimerIntensityValid = true) }
    }

    private fun updateTimerEnvironment(environment: Environment) {
        _timer.update { it.copy(timerEnvironment = environment) }
        _timerState.update { it.copy(isTimerEnvironmentValid = true) }
    }

    private fun updateTimerValidation() {
        Log.i("SyncFitViewModel", "----------------------------------------------------")
        Log.i("SyncFitViewModel", "----------------------------------------------------")
        Log.i("SyncFitViewModel", "Timer Name: ${_timer.value.timerName} : Is Valid: ${_timer.value.timerName.isNotEmpty()}")
        Log.i("SyncFitViewModel", "Timer Intervals: ${_timer.value.timerIntervals} : Is Valid: ${_timer.value.timerIntervals.isNotEmpty()}")
        Log.i("SyncFitViewModel", "Timer Repeats: ${_timer.value.timerRepeats} : Is Valid: ${_timer.value.timerRepeats > 0}")
        Log.i("SyncFitViewModel", "Timer Intensity: ${_timer.value.timerIntensity} : Is Valid: ${_timer.value.timerIntensity != Intensity.NONE}")
        Log.i("SyncFitViewModel", "Timer Environment: ${_timer.value.timerEnvironment} : Is Valid: ${_timer.value.timerEnvironment != Environment.NONE}")

        _timerState.update {
            it.copy(
                isTimerNameValid = _timer.value.timerName.isNotEmpty(),
                isTimerIntervalsValid = _timer.value.timerIntervals.isNotEmpty(),
                isTimerRepeatsValid = _timer.value.timerRepeats > 0,
                isTimerIntensityValid = _timer.value.timerIntensity != Intensity.NONE,
                isTimerEnvironmentValid = _timer.value.timerEnvironment != Environment.NONE,
            )
        }
    }

    private fun updateTimer() {
        updateTimerValidation()

        if (_timerState.value.isTimerNameValid &&
            _timerState.value.isTimerIntervalsValid &&
            _timerState.value.isTimerRepeatsValid &&
            _timerState.value.isTimerIntensityValid &&
            _timerState.value.isTimerEnvironmentValid
        ) {
            val updatedTimer = Timer(
                userId = state.value.userState.user.email,
                timerName = _timer.value.timerName,
                timerIntervals = _timer.value.timerIntervals,
                timerDuration = _timer.value.timerDuration,
                timerRepeats = _timer.value.timerRepeats,
                timerIntensity = _timer.value.timerIntensity,
                timerEnvironment = _timer.value.timerEnvironment,
                timerDateLastUsed = System.currentTimeMillis(),
            )

            _timerState.update {
                it.copy(
                    timer = updatedTimer,
                    isTimerCreateSuccessful = true,
                    timerError = ""
                )
            }

            viewModelScope.launch {
                timerDao.upsertTimer(updatedTimer)
            }
        } else {
            _timerState.update {
                it.copy(
                    isTimerCreateSuccessful = false,
                    timerError = "Please fix errors"
                )
            }
        }
    }

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

    private fun googleSignIn(user: User) {
        viewModelScope.launch {
            val dbUser: User? = userDao.getUserByKey(user.email)
            if (dbUser == null || dbUser.googleUser) {
                userDao.upsertUser(user)
                logIn(user)
            } else {
                _userState.update {
                    it.copy(
                        user = User(),
                        isSignInSuccessful = false,
                        signInError = "Email exists for this account",
                    )
                }

                googleAuthClient.signOut()
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

    private fun updateUser(user: User) {
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
            is AuthEvents.GoogleSignIn -> googleSignIn(event.user)
            is AuthEvents.ExistingUserSignIn -> logIn(event.user)
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
            is UserEvents.UpdateUser -> updateUser(event.user)
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
        when (event) {
            is TimerEvents.CreateTimer -> TODO()
            is TimerEvents.DeleteTimer -> TODO()
            is TimerEvents.GetTimerByKey -> TODO()
            is TimerEvents.GetTimersByUser -> TODO()
            is TimerEvents.UpdateTimer -> updateTimer()
            is TimerEvents.UpdateTimerName -> updateTimerName(event.name)
            is TimerEvents.UpdateTimerIntervals -> updateTimerIntervals(event.intervals)
            is TimerEvents.UpdateTimerRepeats -> updateTimerRepeats(event.repeats)
            is TimerEvents.UpdateTimerIntensity -> updateTimerIntensity(event.intensity)
            is TimerEvents.UpdateTimerEnvironment -> updateTimerEnvironment(event.environment)
        }
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
            isTimerRunning = false,
            timerError = null,
        ) }
    }
}
