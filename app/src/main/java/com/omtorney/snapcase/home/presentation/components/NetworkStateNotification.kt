package com.omtorney.snapcase.home.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.omtorney.snapcase.home.presentation.HomeState
import com.omtorney.snapcase.network.NetworkState

@Composable
fun NetworkStateNotification(
    state: HomeState
) {
    var networkStateText by remember { mutableStateOf("") }
    networkStateText = when (state.networkState) {
        is NetworkState.Available -> "Available"
        is NetworkState.Unavailable -> "Unavailable"
        is NetworkState.Lost -> "Lost"
        is NetworkState.Losing -> "Losing"
    }

    AnimatedVisibility(
        visible = state.networkState == NetworkState.Lost,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 500)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 500)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (state.networkState == NetworkState.Lost ||
                        state.networkState == NetworkState.Unavailable
                    ) {
                        Color.Red.copy(alpha = 0.3f)
                    } else {
                        Color.Green.copy(alpha = 0.3f)
                    }
                )
        ) {
            Text(
                text = if (state.networkState == NetworkState.Lost ||
                    state.networkState == NetworkState.Unavailable
                ) {
                    "Соединение с сетью потеряно (статус: $networkStateText)"
                } else {
                    "Соединение восстановлено"
                },
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 2.dp)
                    .fillMaxWidth()
                ,
                textAlign = TextAlign.Center
            )
        }
    }
}
