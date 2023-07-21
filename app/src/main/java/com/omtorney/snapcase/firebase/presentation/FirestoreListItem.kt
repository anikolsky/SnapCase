package com.omtorney.snapcase.firebase.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun FirestoreListItem(
    userName: String,
    userId: String,
    onUpdateClick: (id: String, name: String) -> Unit,
    onDeleteClick: (id: String, name: String) -> Unit
) {
    val showUpdateDialog = remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Name: $userName",
            modifier = Modifier.clickable { showUpdateDialog.value = true }
        )
        IconButton(onClick = { onDeleteClick(userId, userName) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }
    }
    if (showUpdateDialog.value) {
        FirestoreDialog(
            userName = userName,
            userId = userId,
            showDialog = showUpdateDialog,
            onUpdateClick = { id, name -> onUpdateClick(id, name) }
        )
    }
}
