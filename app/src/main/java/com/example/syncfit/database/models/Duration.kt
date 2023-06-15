package com.example.syncfit.database.models

import kotlinx.serialization.Serializable

@Serializable
data class Duration(
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
) {
    constructor() : this(0, 0, 0)
}
