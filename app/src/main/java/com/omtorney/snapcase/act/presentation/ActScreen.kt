package com.omtorney.snapcase.act.presentation

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.omtorney.snapcase.R
import com.omtorney.snapcase.common.presentation.components.ErrorMessage
import com.omtorney.snapcase.common.presentation.components.LoadingIndicator

@Composable
fun ActScreen(
    state: ActState,
    accentColor: Color
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(bottomBar = {
        BottomAppBar {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        val intent = Intent()
                        intent.action = Intent.ACTION_SEND
                        intent.putExtra(Intent.EXTRA_TEXT, state.text)
                        intent.type = "text/plain"
                        startActivity(
                            context,
                            Intent.createChooser(intent, "${R.string.send}:"),
                            null
                        )
                    },
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = ButtonDefaults.buttonColors(accentColor),
                    enabled = !state.isLoading
                ) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = stringResource(R.string.send).uppercase())
                }
            }
        }
    }) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(text = state.text)
            }
        }
        if (state.isLoading) {
            LoadingIndicator(accentColor = accentColor)
        }
        if (state.error.isNotBlank()) {
            ErrorMessage(message = state.error)
        }

//        val type = "text/plain"
//        val subject = "Your subject"
//        val extraText = "https://www.google.com/codes/"
//        val shareWith = "ShareWith"
//
//        val intent = Intent(Intent.ACTION_SEND)
//        intent.type = type
//        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
//        intent.putExtra(Intent.EXTRA_TEXT, extraText)
//
//        startActivity(context, Intent.createChooser(intent, shareWith), null)

    }
}
