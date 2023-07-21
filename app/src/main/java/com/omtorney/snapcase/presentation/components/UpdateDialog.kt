package com.omtorney.snapcase.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omtorney.snapcase.presentation.theme.SnapCaseTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateDialog(
    downloadProgress: Float,
    onOkClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    AlertDialog(onDismissRequest = { onDismissClick() }) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.extraSmall,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Доступно обновление",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Пожалуйста, установите актуальную версию приложения",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(18.dp))
                if (downloadProgress > 0f && downloadProgress < 1f) {
                    LinearProgressIndicator(
                        progress = downloadProgress,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { onDismissClick() }) {
                        Text(
                            text = "Отмена",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    TextButton(onClick = { onOkClick() }) {
                        Text(
                            text = "Обновить",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun UpdateDialogPreview() {
    SnapCaseTheme {
        Surface {
            UpdateDialog(downloadProgress = 0.25f, onOkClick = {}, onDismissClick = {})
        }
    }
}
