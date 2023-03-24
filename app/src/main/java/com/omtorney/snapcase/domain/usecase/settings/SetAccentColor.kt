package com.omtorney.snapcase.domain.usecase.settings

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.omtorney.snapcase.domain.Repository
import javax.inject.Inject

class SetAccentColor @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(color: Color) {
        repository.setAccentColor(color.toArgb().toLong())
    }
}