package com.example.syncfit.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "users")
data class User(
    @PrimaryKey val email: String,
    var firstname: String? = null,
    var lastname: String? = null,
    var phoneNumber: String? = null,
    var password: String? = null,
    var photoUrl: String? = null,
    var darkMode: Boolean = false,
    val googleUser: Boolean,
) {
    // Empty constructor
    constructor() : this(
        email = "",
        firstname = "",
        lastname = "",
        phoneNumber = "",
        password = "",
        photoUrl = "",
        darkMode = false,
        googleUser = false,
    )

    // Google Users
    constructor(
        email: String,
        firstname: String?,
        phoneNumber: String?,
        photoUrl: String?,
    ) : this(
        email = email,
        firstname = firstname,
        lastname = "",
        phoneNumber = phoneNumber,
        photoUrl = photoUrl,
        googleUser = true,
    )

    // Local users
    constructor(
        email: String,
        firstname: String,
        lastname: String,
        phoneNumber: String,
        password: String,
    ) : this(
        email = email,
        firstname = firstname,
        lastname = lastname,
        phoneNumber = phoneNumber,
        password = password,
        googleUser = false,
    )
}

data class UserWithTimers(
    @Embedded val user: User?,
    @Relation(
        parentColumn = "email",
        entityColumn = "userId",
    )
    val timers: List<Timer>?,
)
