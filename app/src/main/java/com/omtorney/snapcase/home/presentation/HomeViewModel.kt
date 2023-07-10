package com.omtorney.snapcase.home.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.network.NetworkStateManager
import com.omtorney.snapcase.settings.data.SettingsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val settings: SettingsStore,
    private val networkStateManager: NetworkStateManager
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    init {
        viewModelScope.launch {
            settings.selectedCourt.collect { courtTitle ->
                _state.value = state.value.copy(selectedCourt = courtTitle)
            }
        }

        viewModelScope.launch {
            networkStateManager.getNetworkState().collect { networkState ->
                _state.value = state.value.copy(networkState = networkState)
            }
        }
    }

//    val selectedCourt = repository.getSelectedCourt.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000L),
//        initialValue = Courts.Dmitrov.title
//    )

//    val networkState: StateFlow<NetworkState> = networkStateManager.getNetworkState().stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000L),
//        initialValue = NetworkState.Lost
//    )

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SetSelectedCourt -> {
                viewModelScope.launch {
                    settings.setSelectedCourt(event.courtTitle)
                }
            }
        }
    }
}
