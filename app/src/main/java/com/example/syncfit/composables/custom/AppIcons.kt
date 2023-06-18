package com.example.syncfit.composables.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.syncfit.R

@Composable
fun LogoNoText(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier,
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Logo",
        tint = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun LogoWithText(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "Sync Fit",
            fontSize = MaterialTheme.typography.displayLarge.fontSize,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
