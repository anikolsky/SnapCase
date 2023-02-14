package com.omtorney.snapcase.presentation.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    content: @Composable (RowScope.() -> Unit)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        content()
    }
}

@Composable
fun BackButton(
    onBackClick: () -> Unit
) {
    IconButton(onClick = onBackClick) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = "",
            tint = contentColorFor(backgroundColor = MaterialTheme.colors.background)
        )
    }
}

@Composable
fun TopBarTitle(
    modifier: Modifier = Modifier,
    @StringRes title: Int
) {
    Text(
        text = LocalContext.current.getString(title),
        style = MaterialTheme.typography.h6.merge(
            TextStyle(color = contentColorFor(backgroundColor = MaterialTheme.colors.background))
        ),
        modifier = modifier.padding(start = 8.dp)
    )
}

@Composable
fun MoreButton() {
    IconButton(onClick = {}) {
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = "",
            tint = contentColorFor(backgroundColor = MaterialTheme.colors.background)
        )
    }
}

@Composable
fun SettingsButton(onSettingsClick: () -> Unit) {
    IconButton(onClick = onSettingsClick) {
        Icon(
            imageVector = Icons.Rounded.Settings,
            contentDescription = "",
            tint = contentColorFor(backgroundColor = MaterialTheme.colors.background)
        )
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TopBarPreview() {
    TopBar(content = {})
}