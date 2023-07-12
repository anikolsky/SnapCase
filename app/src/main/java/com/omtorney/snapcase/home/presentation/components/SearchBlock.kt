package com.omtorney.snapcase.home.presentation.components

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omtorney.snapcase.R
import com.omtorney.snapcase.common.presentation.theme.SnapCaseTheme

@Composable
fun SearchBlock(
    accentColor: Color,
    shape: CornerBasedShape,
    onSearchClick: (String) -> Unit
) {
    var input by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    BlockFrame(accentColor = accentColor) {
        OutlinedTextField(
            value = input,
            placeholder = {
                Text(
                    text = stringResource(R.string.enter_participant_or_number),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            },
            onValueChange = { input = it },
            singleLine = true,
            maxLines = 1,
            shape = shape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = accentColor,
                unfocusedBorderColor = accentColor,
                focusedContainerColor = accentColor.copy(alpha = 0.2f),
                unfocusedContainerColor = accentColor.copy(alpha = 0.2f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth(),
            shape = shape,
            colors = ButtonDefaults.buttonColors(accentColor),
            onClick = {
                if (input.isEmpty()) {
                    Toast.makeText(
                        context,
                        R.string.enter_participant_or_number,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    onSearchClick(input)
                }
            }
        ) {
            Text(
                text = stringResource(R.string.search_cases).uppercase(),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchBlockPreview() {
    SnapCaseTheme {
        Surface {
            SearchBlock(
                accentColor = Color.Gray,
                shape = MaterialTheme.shapes.extraSmall,
                onSearchClick = {}
            )
        }
    }
}
