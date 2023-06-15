package com.example.syncfit.composables.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = DividerDefaults.color,
    width: Float = CustomDividerDefaults.Width
) {
    val targetThickness = if (thickness == Dp.Hairline) {
        (1f / LocalDensity.current.density).dp
    } else {
        thickness
    }
    Box(
        modifier
            .fillMaxWidth(width)
            .height(targetThickness)
            .background(color = color)
    )
}

object CustomDividerDefaults {
    const val Width: Float = 0.8f
}
