package com.example.syncfit

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.toMutableStateList
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
    private val _timerIntervals = MutableStateFlow(_timer.value.timerIntervals)

    val timerIntervals = _timerIntervals.asStateFlow()

    val state = combine(_state, _userState, _timerState) { state, userState, timerState ->
        state.copy(
            userState = userState,
            timerState = timerState,
        )
    }.stateIn(viewModelScope,  SharingStarted.WhileSubscribed(5000), AppState())

    private fun addTimerInterval(interval: Interval) {
        _timerIntervals.update {
            it.toMutableStateList().apply {
                add(interval)
            }
        }
        updateTimerIntervals()
    }

    private fun deleteTimerInterval(interval: Interval) {
        _timerIntervals.update {
            it.toMutableStateList().apply {
                remove(interval)
            }
        }
        updateTimerIntervals()
    }

    private fun updateTimerIntervals() {
        _timer.update { it.copy(timerIntervals = _timerIntervals.value) }
        _timerState.update { it.copy(isTimerIntervalsValid = true) }
    }

    private fun timersList() {
        viewModelScope.launch {
            timerDao.getTimersByUser(state.value.userState.user.email).collect { timers ->
                if (timers.timers.isNotEmpty()) _timerState.update {
                    it.copy(
                        timers = timers.timers,
                    )
                }
            }
        }
    }

    private fun updateTimerValidation(timer: Timer) {
        Log.i("SyncFitViewModel", "----------------------------------------------------")
        Log.i("SyncFitViewModel", "----------------------------------------------------")
        Log.i("SyncFitViewModel", "Timer Name: ${timer.timerName} : Is Valid: ${timer.timerName.isNotEmpty()}")
        Log.i("SyncFitViewModel", "Timer Intervals: ${_timerIntervals.value} : Is Valid: ${_timerIntervals.value.isNotEmpty()}")
        Log.i("SyncFitViewModel", "Timer Repeats: ${timer.timerRepeats} : Is Valid: ${timer.timerRepeats > 0}")
        Log.i("SyncFitViewModel", "Timer Intensity: ${timer.timerIntensity} : Is Valid: ${timer.timerIntensity != Intensity.NONE}")
        Log.i("SyncFitViewModel", "Timer Environment: ${timer.timerEnvironment} : Is Valid: ${timer.timerEnvironment != Environment.NONE}")

        _timerState.update {
            it.copy(
                isTimerNameValid = timer.timerName.isNotEmpty(),
                isTimerIntervalsValid = _timerIntervals.value.isNotEmpty(),
                isTimerRepeatsValid = timer.timerRepeats > 0,
                isTimerIntensityValid = timer.timerIntensity != Intensity.NONE,
                isTimerEnvironmentValid = timer.timerEnvironment != Environment.NONE,
            )
        }
    }

    private fun updateTimer(timer: Timer, mode: String) {
        updateTimerValidation(timer)
        println(timer.timerId)

        val duration = _timerIntervals.value.sumOf { it.intervalTimeStamp }

        val updatedTimer = Timer(
            userId = state.value.userState.user.email,
            timerId = timer.timerId,
            timerName = timer.timerName,
            timerIntervals = _timerIntervals.value,
            timerRepeats = timer.timerRepeats,
            timerTimeStamp = duration,
            timerIntensity = timer.timerIntensity,
            timerEnvironment = timer.timerEnvironment,
            timerDateLastUsed = System.currentTimeMillis(),
        )

        if (_timerState.value.isTimerNameValid &&
            _timerState.value.isTimerIntervalsValid &&
            _timerState.value.isTimerRepeatsValid &&
            _timerState.value.isTimerIntensityValid &&
            _timerState.value.isTimerEnvironmentValid
        ) {
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

            if (mode == "create") {
                _timerState.update {
                    it.copy(
                        timer = Timer(),
                        isTimerCreateSuccessful = true,
                        timerError = ""
                    )
                }
                _timerIntervals.update { mutableListOf() }
            }

        } else {
            _timerState.update {
                it.copy(
                    timer = updatedTimer,
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
            }
        }
    }

    private fun googleSignIn(user: User) {
        viewModelScope.launch {
            val dbUser: User? = userDao.getUserByKey(user.email)
            if (dbUser == null || dbUser.googleUser) {
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
        viewModelScope.launch {
            val dbUser: User? = userDao.getUserByKey(user.email)
            if (dbUser == null) {
                _userState.update {
                    it.copy(
                        user = user,
                        isSignInSuccessful = true,
                        signInError = null,
                    )
                }
                userDao.upsertUser(user)

            } else {
                _userState.update {
                    it.copy(
                        user = dbUser,
                        isSignInSuccessful = true,
                        signInError = null,
                    )
                }
                userDao.upsertUser(dbUser)
            }

            timersList()
        }
    }

    private fun userValidation(email: String, password: String) {
        viewModelScope.launch {
            val dbUser: User? = userDao.getUserByKey(email)
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

    private fun deleteTimer(timer: Timer) {
        viewModelScope.launch {
            timerDao.deleteTimer(timer)
            timersList()
        }
    }

    private fun getTimer(timerId: Int) {
        val timer = state.value.timerState.timers.find { it.timerId == timerId }
        if (timer != null) {
            _timerState.update {
                it.copy(
                    timer = timer,
                    isTimerCreateSuccessful = false,
                    timerError = ""
                )
            }
            _timerIntervals.update { timer.timerIntervals.toMutableList() }

            println("Timer: $timer")
            println("Timer Intervals: ${timer.timerIntervals}")
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
    }

    private fun timerEvent(event: TimerEvents) {
        when (event) {
            is TimerEvents.CreateTimer -> TODO()
            is TimerEvents.DeleteTimer -> deleteTimer(event.timer)
            is TimerEvents.GetTimerByKey -> TODO()
            is TimerEvents.GetTimersByUser -> TODO()
            is TimerEvents.UpdateTimer -> updateTimer(event.timer, event.mode)
            is TimerEvents.AddTimerInterval -> addTimerInterval(event.interval)
            is TimerEvents.DeleteTimerInterval -> deleteTimerInterval(event.interval)
            is TimerEvents.UpdateTimerIntervals -> updateTimerIntervals()
            is TimerEvents.GetTimer -> getTimer(event.timerId)
        }
    }

    private fun resetTimerIntervals() {
        _timerIntervals.update { emptyList() }
    }

    fun resetCreate() {
        _timerState.update { it.copy(
            timers = state.value.timerState.timers,
            timer = Timer(),
            isTimerCreateSuccessful = false,
            timerError = null,
        ) }

        resetTimerIntervals()
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
