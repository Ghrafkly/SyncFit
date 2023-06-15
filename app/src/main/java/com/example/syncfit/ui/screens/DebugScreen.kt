//package com.example.syncfit.ui.screens
//
//import android.annotation.SuppressLint
//import android.util.Log
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.Button
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.navigation.NavController
//import com.example.syncfit.database.entities.Timer
//import com.example.syncfit.database.entities.User
//import com.example.syncfit.events.AppEvents
//import com.example.syncfit.events.TimerEvents
//import com.example.syncfit.events.UserEvents
//import com.example.syncfit.database.models.Duration
//import com.example.syncfit.database.models.Environment
//import com.example.syncfit.database.models.Intensity
//import com.example.syncfit.database.models.Interval
//import com.example.syncfit.events.ClickEvents
//import com.example.syncfit.states.AppState
//
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
////@Preview(device = "id:pixel_4", showBackground = true, showSystemUi = true)
//@Composable
//fun DebugScreen(
//    modifier: Modifier = Modifier,
//    navController: NavController,
//    onEvent: (AppEvents) -> Unit,
//    state: AppState,
//) {
//    val userState = state.userState
//    val timerState = state.timerState
//
//    Scaffold(
//        modifier = modifier.fillMaxSize(),
//        content = {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Button(onClick = {
//                    onEvent(UserEvents.CreateUser(user))
//                    Log.i("state", state.toString())
//                    Log.i("state", user.toString())
//                }) {
//                    Text(text = "Create user")
//                }
//
//                Button(onClick = {
//                    onEvent(UserEvents.UpdateUser(updateUser(user)))
//                    Log.i("state", state.toString())
//                    Log.i("state", user.toString())
//                }) {
//                    Text(text = "Update user")
//                }
//
//                Button(onClick = {
//                    onEvent(UserEvents.GetUserByKey("kylesharplol@gmail.com"))
//                    Log.i("state", state.toString())
//                    Log.i("state", user.toString())
//                }) {
//                    Text(text = "Get user")
//                }
//
//                Button(onClick = {
//                    Log.i("state", state.toString())
//                    Log.i("state", user.toString())
//                    onEvent(UserEvents.DeleteUser(user))
//                }) {
//                    Text(text = "Delete user")
//                }
//
//                Button(onClick = { onEvent(TimerEvents.CreateTimer(timer)) }) {
//                    Text(text = "Create timer")
//                }
//
//                Button(onClick = { onEvent(TimerEvents.UpdateTimer(updateTimer(timer))) }) {
//                    Text(text = "Update timer")
//                }
//
//                Button(onClick = { onEvent(TimerEvents.GetTimersByUser(user)) }) {
//                    Text(text = "Get timers by user")
//                }
//
//                Button(onClick = { onEvent(TimerEvents.GetTimerByKey(timer)) }) {
//                    Text(text = "Get timer by key")
//                }
//
//                Button(onClick = { onEvent(TimerEvents.DeleteTimer(timer)) }) {
//                    Text(text = "Delete timer")
//                }
//
//            }
//        }
//    )
//}
//
//private var user = User(
//    email = "kylesharplol@gmail.com",
//    firstname = "Kyle",
//    lastname = "Sharp",
//    password = "password",
//    phoneNumber = "1234567890",
//    darkMode = false,
//    googleUser = false,
//)
//
//fun updateUser(user: User): User {
//    val userCopy = user.copy()
//
//    userCopy.firstname = "John"
//    userCopy.lastname = "Doe"
//
//    return userCopy
//}
//
//private val duration = Duration(
//    hours = 0,
//    minutes = 1,
//    seconds = 20,
//)
//
//private val warmup = Interval(
//    intervalId = 0,
//    intervalName = "Warmup",
//    intervalDuration = duration,
//)
//
//private val work = Interval(
//    intervalId = 1,
//    intervalName = "Work",
//    intervalDuration = duration,
//)
//
//private val cooldown = Interval(
//    intervalId = 2,
//    intervalName = "Cool down",
//    intervalDuration = duration,
//)
//
//private val intervals = listOf(warmup, work, cooldown)
//
//fun sumOfDurations(inter: List<Interval>) = inter.map { it.intervalDuration }.reduce { acc, duration ->
//    var hours = acc.hours + duration.hours
//    var minutes = acc.minutes + duration.minutes
//    var seconds = acc.seconds + duration.seconds
//
//    if (seconds >= 60) {
//        val extraMinutes = seconds / 60
//
//        minutes += extraMinutes
//        seconds -= extraMinutes * 60
//    }
//
//    if (minutes >= 60) {
//        val extraHours = minutes / 60
//
//        hours += extraHours
//        minutes -= extraHours * 60
//    }
//
//    Duration(hours, minutes, seconds)
//}
//
//private val timer = Timer(
//    timerId = 2,
//    userId = "kylesharplol@gmail.com",
//    timerName = "Test timer",
//    timerIntervals = intervals,
//    timerDuration = sumOfDurations(intervals),
//    timerRepeats = 1,
//    timerIntensity = Intensity.HIGH,
//    timerEnvironment = Environment.INDOOR,
//    timerDateCreated = System.currentTimeMillis(),
//    timerDateLastUsed = System.currentTimeMillis(),
//    timerTimesUsed = 0,
//)
//
//fun updateTimer(timer: Timer): Timer {
//    timer.timerName = "Updated timer"
//    timer.timerIntervals = listOf(
//        Interval(
//            intervalName = "Warmup2",
//            intervalDuration = Duration(
//                hours = 0,
//                minutes = 0,
//                seconds = 5,
//            ),
//        ),
//        Interval(
//            intervalName = "Work2",
//            intervalDuration = Duration(
//                hours = 0,
//                minutes = 0,
//                seconds = 5,
//            ),
//        ),
//        Interval(
//            intervalName = "Cool down2",
//            intervalDuration = Duration(
//                hours = 0,
//                minutes = 0,
//                seconds = 5,
//            ),
//        ),
//    )
//    timer.timerDuration = sumOfDurations(timer.timerIntervals)
//    timer.timerRepeats = 2
//    timer.timerIntensity = Intensity.LOW
//    timer.timerEnvironment = Environment.OUTDOOR
//    timer.timerDateLastUsed = System.currentTimeMillis()
//    timer.timerTimesUsed = 1
//
//    return timer
//}
