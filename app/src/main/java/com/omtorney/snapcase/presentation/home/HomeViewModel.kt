package com.omtorney.snapcase.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.domain.usecase.settings.SettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {

    val selectedCourt = settingsUseCases.getSelectedCourt.invoke().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Courts.Dmitrov.title
    )

    fun setSelectedCourt(courtTitle: String) {
        viewModelScope.launch {
            settingsUseCases.setSelectedCourt(courtTitle)
        }
    }
}