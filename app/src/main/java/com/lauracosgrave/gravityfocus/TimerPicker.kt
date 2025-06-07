package com.lauracosgrave.gravityfocus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun TimerPicker(
    timeDisplay: String,
    changeHandler: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row() {
        NumberInput(
            value = timeDisplay,
            onValueChange = { newValue -> changeHandler(newValue) },
            label = "Enter number of minutes for timer"
        )
    }
}

@Composable
fun TimerPickerDialog(
    defaultTime: Int,
    onConfirm: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var timeDisplay by rememberSaveable { mutableStateOf(defaultTime.toString()) }


    Dialog(onDismissRequest = { onDismissRequest() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    text = "Edit the length of the timer",
                    modifier = Modifier.padding(16.dp),
                )
                TimerPicker(timeDisplay, changeHandler = { newValue -> timeDisplay = newValue })
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = {
                            val newTime = if (timeDisplay.isNotEmpty()) timeDisplay.toInt() else 0
                            onConfirm(newTime)
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }

}


@Composable
fun NumberInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Enter number"
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            // Accept only digits
            if (newValue.all { it.isDigit() }) {
                onValueChange(newValue)
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}