package com.omtorney.snapcase.common.presentation.components

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
    accentColor: Long,
    onBackClick: () -> Unit
) {
    IconButton(onClick = onBackClick) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = "",
            tint = Color(accentColor)
        )
    }
}

@Composable
fun TopBarTitle(
    modifier: Modifier = Modifier,
    accentColor: Long,
    @StringRes title: Int
) {
    Text(
        text = LocalContext.current.getString(title),
        style = MaterialTheme.typography.h6.merge(
            TextStyle(color = Color(accentColor))
        ),
        modifier = modifier.padding(start = 10.dp)
    )
}

@Composable
fun MoreButton(accentColor: Long) {
    IconButton(onClick = {}) {
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = "",
            tint = contentColorFor(backgroundColor = Color(accentColor))
        )
    }
}

@Composable
fun SettingsButton(
    accentColor: Long,
    onSettingsClick: () -> Unit
) {
    IconButton(onClick = onSettingsClick) {
        Icon(
            imageVector = Icons.Rounded.Settings,
            contentDescription = "",
            tint = Color(accentColor)
        )
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TopBarPreview() {
    TopBar(content = {})
}