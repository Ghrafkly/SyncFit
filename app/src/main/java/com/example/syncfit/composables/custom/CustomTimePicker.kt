package com.example.syncfit.composables.custom

import androidx.activity.result.IntentSenderRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.text.isDigitsOnly
import com.example.syncfit.events.AppEvents
import com.example.syncfit.fromTimeStamp
import com.example.syncfit.states.AppState

@Composable
fun CustomTimePicker(
    modifier: Modifier = Modifier,
    onEvent: (AppEvents) -> Unit = {},
    state: AppState = AppState(),
    onDismissRequest: () -> Unit = {},
    onConfirm: (String) -> Unit = {},
) {
    val time = state.timerState.timer.timerTimeStamp.fromTimeStamp()
    val timeParts = time.split(":")

    val minutes = timeParts[1]
    val seconds = timeParts[2]

    var minuteInput by remember { mutableStateOf("") }
    var secondInput by remember { mutableStateOf("") }

    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(
            focusable = true
        ),
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                .border(1.dp, color = Color.Black, RoundedCornerShape(10.dp))
        ) {
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Select Time",
                )
                Spacer(modifier = Modifier.size(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextField(
                        modifier = Modifier.width(75.dp),
                        value = minuteInput,
                        onValueChange = {
                            minuteInput = if (it.isEmpty()) {
                                it
                            } else {
                                when (it.isDigitsOnly() && it.toInt() < 60) {
                                    true -> it
                                    false -> minuteInput
                                }
                            }
                        },
                        textStyle = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                        ),
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = ":",
                        fontSize = 24.sp,
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    TextField(
                        modifier = Modifier.width(75.dp),
                        value = secondInput,
                        onValueChange = {
                            secondInput = if (it.isEmpty()) {
                                it
                            } else {
                                when (it.isDigitsOnly() && it.toInt() < 60) {
                                    true -> it
                                    false -> secondInput
                                }
                            }
                        },
                        textStyle = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                        ),
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                CustomDivider(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                )
                Spacer(modifier = Modifier.size(16.dp))
                Row {
                    Button(onClick = {
                        if (minuteInput.isEmpty()) {
                            minuteInput = "00"
                        }

                        if (secondInput.isEmpty()) {
                            secondInput = "00"
                        }

                        onConfirm("$minuteInput:$secondInput")
                        onDismissRequest()
                    }) {
                        Text(text = "Confirm")
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    Button(onClick = {
                        onDismissRequest()
                    }) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
    }
}
