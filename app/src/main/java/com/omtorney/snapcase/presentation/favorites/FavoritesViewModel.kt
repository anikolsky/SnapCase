package com.omtorney.snapcase.presentation.favorites

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.data.database.CaseDao
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.domain.usecase.CaseUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val caseDao: CaseDao,
    private val caseUseCases: CaseUseCases
) : ViewModel() {

    private val _state = mutableStateOf(FavoritesState())
    val state: State<FavoritesState> = _state

    private var getFavoriteCasesJob: Job? = null

    private fun getFavoriteCases() {
        getFavoriteCasesJob?.cancel()
        getFavoriteCasesJob = caseUseCases.getFavoriteCases().onEach { cases ->
            _state.value = state.value.copy(cases = cases)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: FavoritesEvent) {
        when (event) {
            is FavoritesEvent.Delete -> {
                viewModelScope.launch {
                    caseUseCases.deleteCase(event.case)
                }
            }
            is FavoritesEvent.Refresh -> {
                viewModelScope.launch {
                    event.cases.forEach { case ->
                        caseUseCases.fillCase(case, Courts.Dmitrov)
                        caseUseCases.updateCase(case)
                    }
                }
            }
        }
    }
}