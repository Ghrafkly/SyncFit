package com.example.syncfit.events

import com.example.syncfit.database.entities.User

sealed interface UserEvents: AppEvents {
    data class CreateUser(val user: User): UserEvents
    object DeleteUser: UserEvents
    data class UpdateUser(val user: User): UserEvents
    data class GetUserByKey(val key: String): UserEvents
}
