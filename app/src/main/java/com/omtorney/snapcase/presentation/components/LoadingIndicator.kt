package com.omtorney.snapcase.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.omtorney.snapcase.R

@Composable
fun LoadingIndicator(accentColor: Color) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading))
    Box(modifier = Modifier.fillMaxSize()) {
        LottieAnimation(
            composition = composition,
            modifier = Modifier.align(Alignment.Center)
        )
//        CircularProgressIndicator(
//            color = accentColor,
//            modifier = Modifier.align(Alignment.Center)
//        )
    }
}
