package com.omtorney.snapcase.firebase.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FirestoreDialog(
    userName: String,
    userId: String,
    showDialog: MutableState<Boolean>,
    onUpdateClick: (id: String, name: String) -> Unit,
) {
    var inputText by remember { mutableStateOf(userName) }

    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        text = {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                shape = MaterialTheme.shapes.extraSmall
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onUpdateClick(userId, inputText)
                    showDialog.value = false
                },
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Text(text = "Update")
            }
        },
        shape = MaterialTheme.shapes.extraSmall,
        modifier = Modifier.padding(8.dp)
    )
}
