package com.omtorney.snapcase.favorites.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.common.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _state = mutableStateOf(FavoritesState())
    val state: State<FavoritesState> = _state

    private var getFavoriteCasesJob: Job? = null

    init {
        getFavoriteCases()
    }

    private fun getFavoriteCases() {
        getFavoriteCasesJob?.cancel()
        getFavoriteCasesJob = useCases.getFavoriteCases().onEach { cases ->
            _state.value = state.value.copy(cases = cases)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: FavoritesEvent) {
        when (event) {
            is FavoritesEvent.Delete -> {
                viewModelScope.launch {
                    useCases.deleteCase(event.case)
                }
            }
//            is FavoritesEvent.Refresh -> {
//                viewModelScope.launch {
//                    event.cases.forEach { case ->
//                        useCases.fetchCase(case, case.court) // add new field?
//                    }
//                }
//            }
        }
    }
}