package com.omtorney.snapcase.presentation.screen.settings.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.omtorney.snapcase.data.remote.database.FirestoreUser
import com.omtorney.snapcase.firebase.auth.UserData
import com.omtorney.snapcase.firebase.presentation.FirestoreListItem
import com.omtorney.snapcase.firebase.presentation.FirestoreUserState
import com.omtorney.snapcase.presentation.screen.settings.SettingsEvent

@Composable
fun SignInCard(
    accentColor: Color,
    userData: UserData?,
    firestoreUserState: FirestoreUserState,
    onEvent: (SettingsEvent) -> Unit
) {
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .border(
                width = 1.dp,
                color = if (userData != null) Color.Cyan.copy(alpha = 0.5f) else accentColor,
                shape = RoundedCornerShape(6.dp)
            )
    ) {
        Text(
            text = "Аккаунт: ${userData?.name ?: "не используется"}",
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        )
        Button(
            enabled = userData?.name != null,
            onClick = {
                if (userData != null) {
                    onEvent(SettingsEvent.CreateBackup(userData.name!!, context))
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan.copy(alpha = 0.5f)),
            shape = MaterialTheme.shapes.extraSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 0.dp)
        ) {
            Text("Сохранение ${userData?.name ?: "недоступно"}")
        }
        when {
            firestoreUserState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            !firestoreUserState.errorMessage.isNullOrEmpty() -> {
                Text(
                    text = firestoreUserState.errorMessage,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            firestoreUserState.data == null -> {
                Text(
                    text = "Empty",
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            firestoreUserState.data != null -> {
                val user = firestoreUserState.data
                FirestoreListItem(
                    userName = user.name.toString(),
                    userId = user.id.toString(),
                    onUpdateClick = { id, name ->
                        onEvent(SettingsEvent.UpdateBackup(FirestoreUser(id, name), context))
                    },
                    onDeleteClick = { id, name ->
                        onEvent(SettingsEvent.DeleteBackup(FirestoreUser(id, name), context))
                    }
                )
            }
        }
    }
}
