package com.omtorney.snapcase.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.data.local.SettingsStore
import com.omtorney.snapcase.domain.court.Courts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val settingsDataStore: SettingsStore
) : ViewModel() {

    val selectedCourt = settingsDataStore.getSelectedCourt.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Courts.Dmitrov.title
    )

    fun setSelectedCourt(courtTitle: String) {
        viewModelScope.launch {
            settingsDataStore.setSelectedCourt(courtTitle)
        }
    }
}