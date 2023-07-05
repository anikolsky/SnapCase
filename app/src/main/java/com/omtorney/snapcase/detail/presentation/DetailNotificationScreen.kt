package com.omtorney.snapcase.detail.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omtorney.snapcase.common.presentation.theme.SnapCaseTheme

@Composable
fun DetailNotificationScreen(
    accentColor: Color,
    courtTitle: String,
    number: String,
    event: String,
    participants: String,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.padding(24.dp)) {

        Text(
            text = "Обновление информации по делу № $number",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )
        Divider(modifier = Modifier.padding(vertical = 24.dp))
        Text(
            text = event,
            style = MaterialTheme.typography.titleLarge
        )
        Divider(modifier = Modifier.padding(vertical = 24.dp))
        Text(
            text = "Суд",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = courtTitle)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Стороны",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = participants)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            Button(
                onClick = onClick,
                shape = MaterialTheme.shapes.extraSmall,
                colors = ButtonDefaults.buttonColors(accentColor),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text(text = "Перейти в карточку дела".uppercase())
            }
        }
    }
}

@Preview
@Composable
fun DetailNotificationScreenPreview() {
    SnapCaseTheme {
        Surface {
            DetailNotificationScreen(
                accentColor = Color.Blue,
                courtTitle = "Дмитровский городской",
                number = "2-2222/2022",
                event = "Подана жалоба",
                participants = "АДМИНИСТРАТИВНЫЙ ИСТЕЦ: Иванов И.И.\nАДМИНИСТРАТИВНЫЙ ОТВЕТЧИК: Петров П.П.",
                onClick = {}
            )
        }
    }
}