package com.omtorney.snapcase.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.common.domain.Repository
import com.omtorney.snapcase.common.domain.court.Courts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val selectedCourt = repository.getSelectedCourt.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Courts.Dmitrov.title
    )

    fun setSelectedCourt(courtTitle: String) {
        viewModelScope.launch {
            repository.setSelectedCourt(courtTitle)
        }
    }
}