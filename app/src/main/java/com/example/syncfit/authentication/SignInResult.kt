package com.example.syncfit.authentication

import com.example.syncfit.database.entities.User

data class SignInResult(
    val user: User?,
    val errorMessage: String?
)
