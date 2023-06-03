package com.omtorney.snapcase.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.omtorney.snapcase.R

@Composable
fun ScheduleBlock(
    date: String,
    court: String,
    accentColor: Color,
    shape: CornerBasedShape,
    onOpenClick: () -> Unit,
    onScheduleClick: (String, String) -> Unit
) {
    BlockFrame(accentColor = accentColor) {
        OutlinedButton(
            onClick = onOpenClick,
            shape = shape,
            border = BorderStroke(
                width = 1.dp,
                color = accentColor
            ),
            colors = ButtonDefaults.outlinedButtonColors(accentColor.copy(alpha = 0.2f)),
            modifier = Modifier
                .height(46.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = date,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth(),
            shape = shape,
            colors = ButtonDefaults.buttonColors(accentColor),
            onClick = { onScheduleClick(date, court) }
        ) {
            Text(
                text = stringResource(R.string.show_schedule).uppercase(),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
