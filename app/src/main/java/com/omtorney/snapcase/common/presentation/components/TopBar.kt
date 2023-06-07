package com.omtorney.snapcase.common.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omtorney.snapcase.R
import com.omtorney.snapcase.common.presentation.theme.SnapCaseTheme

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
//    accentColor: Color,
    onBackClick: () -> Unit
) {
    IconButton(onClick = onBackClick) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = "Back",
//            tint = accentColor
        )
    }
}

@Composable
fun TopBarTitle(
    modifier: Modifier = Modifier,
//    accentColor: Color,
    @StringRes title: Int
) {
    Text(
        text = LocalContext.current.getString(title),
        style = MaterialTheme.typography.titleLarge
//            .merge(
//            TextStyle(color = Color(accentColor))
//        )
        ,
        modifier = modifier.padding(start = 10.dp)
    )
}

@Composable
fun FilterButton(
    onClick: () -> Unit,
    title: @Composable (() -> Unit)? = null
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onBackground)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_round_filter_list),
                contentDescription = "Filter"
            )
            Spacer(modifier = Modifier.width(4.dp))
            title?.invoke()
        }
    }
}

@Composable
fun SettingsButton(
//    accentColor: Long,
    onSettingsClick: () -> Unit
) {
    IconButton(onClick = onSettingsClick) {
        Icon(
            imageVector = Icons.Rounded.Settings,
            contentDescription = "Settings",
//            tint = Color(accentColor)
        )
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TopBarPreview() {
    SnapCaseTheme {
        Surface {
            TopBar(content = {
                FilterButton(onClick = {}) {
                    Text(text = "Text")
                }
            })
        }
    }
}
