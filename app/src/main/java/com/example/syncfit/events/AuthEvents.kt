package com.example.syncfit.events

import com.example.syncfit.database.entities.User

sealed interface AuthEvents: AppEvents {
    data class LocalSignIn(val email: String, val password: String): AuthEvents
    data class GoogleSignIn(val user: User): AuthEvents
    data class CreateAccount(val user: User): AuthEvents
    data class ExistingUserSignIn(val user: User): AuthEvents

    object ResetSignIn: AuthEvents
    object LogOut: AuthEvents
}
