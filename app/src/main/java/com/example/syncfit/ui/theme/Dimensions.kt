package com.example.syncfit.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

sealed class Dimensions(val small: Dp, val medium: Dp, val large: Dp) {
    object ButtonWidth: Dimensions(
        small = 150.dp,
        medium = 210.dp,
        large = 320.dp,
    )

    object ButtonHeight: Dimensions(
        small = 40.dp,
        medium = 50.dp,
        large = 60.dp,
    )

    object Spacing: Dimensions(
        small = 8.dp,
        medium = 16.dp,
        large = 24.dp,
    )
}
