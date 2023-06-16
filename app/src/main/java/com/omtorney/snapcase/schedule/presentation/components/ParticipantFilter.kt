package com.omtorney.snapcase.schedule.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.omtorney.snapcase.R

@Composable
fun ParticipantFilter(
    query: String,
    accentColor: Color,
    onQueryChange: (String) -> Unit,
    onResetClick: () -> Unit,
) {
    Text(
        text = "По участнику",
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(IntrinsicSize.Max)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(start = 8.dp, end = 0.dp, top = 8.dp, bottom = 0.dp)
                .border(
                    width = 1.dp,
                    color = accentColor,
                    shape = MaterialTheme.shapes.extraSmall
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                maxLines = 1,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                value = query,
                onValueChange = { onQueryChange(it) },
                modifier = Modifier
                    .padding(start = 12.dp)
                    .fillMaxWidth()
            )
        }
        TextButton(
            onClick = { onResetClick() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 8.dp, top = 4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_round_restart_alt),
                    contentDescription = "Reset participant",
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Сброс",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
