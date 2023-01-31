package com.omtorney.snapcase.presentation.act

import android.content.Intent
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ActScreen(
    viewModel: ActViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    val context = LocalContext.current

    Text(text = state.text.toString())

//    actLines.forEach { binding.actText.append("$it\n\n") }

    Button(onClick = {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, state.text.toString())
        intent.type = "text/plain"
        startActivity(context, Intent.createChooser(intent, "Send to:"), null)
    }) {
        Text(text = "Отправить")
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