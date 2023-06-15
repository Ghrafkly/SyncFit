package com.example.syncfit.states

import com.example.syncfit.database.entities.User

data class UserState(
    var user: User = User(),
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
)
