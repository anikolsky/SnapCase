package com.omtorney.snapcase.home.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.common.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    init {
        viewModelScope.launch {
            repository.getSelectedCourt.collect { courtTitle ->
                _state.value = state.value.copy(selectedCourt = courtTitle)
            }
        }
    }

//    val selectedCourt = repository.getSelectedCourt.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000L),
//        initialValue = Courts.Dmitrov.title
//    )

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SetSelectedCourt -> {
                viewModelScope.launch {
                    repository.setSelectedCourt(event.courtTitle)
                }
            }
        }
    }
}
