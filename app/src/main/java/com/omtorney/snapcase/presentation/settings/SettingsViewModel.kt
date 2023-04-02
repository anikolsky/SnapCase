package com.omtorney.snapcase.presentation.settings

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.data.local.SettingsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsStore
) : ViewModel() {

    fun setAccentColor(color: Color) = viewModelScope.launch {
        settingsDataStore.setAccentColor(color.toArgb().toLong())
    }
}