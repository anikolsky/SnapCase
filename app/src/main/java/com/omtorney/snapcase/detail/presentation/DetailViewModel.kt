package com.omtorney.snapcase.detail.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.common.domain.court.Courts
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.domain.model.ProcessStep
import com.omtorney.snapcase.common.domain.usecase.CaseUseCases
import com.omtorney.snapcase.common.presentation.components.UiEvent
import com.omtorney.snapcase.common.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val caseUseCases: CaseUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(DetailState())
    val state: State<DetailState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<String>("caseNumber")?.let { caseNumber ->
            val caseNumberParam = caseNumber.replace("+", "/")
            viewModelScope.launch {
                _state.value = DetailState(case = caseUseCases.getCaseByNumber(caseNumberParam) ?: Case())
                onEvent(DetailEvent.Fill)
            }
        }
    }

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.Save -> {
                viewModelScope.launch {
                    val case = event.case.copy(isFavorite = true)
                    caseUseCases.saveCase(case)
                    _eventFlow.emit(UiEvent.Save)
                }
            }
            is DetailEvent.Delete -> {
                viewModelScope.launch {
                    caseUseCases.deleteCase(event.case)
                    _eventFlow.emit(UiEvent.Delete)
                }
            }
            is DetailEvent.Fill -> {
                viewModelScope.launch {
                    caseUseCases.fillCase(state.value.case, Courts.Dmitrov).collect { result ->
                        when (result) {
                            is Resource.Loading -> {
                                _state.value = state.value.copy(isLoading = true)
                            }
                            is Resource.Success -> {
                                _state.value = state.value.copy(case = result.data ?: Case(), isLoading = false)
                            }
                            is Resource.Error -> {
                                _state.value =
                                    state.value.copy(
                                        error = result.message ?: "Unexpected error",
                                        isLoading = false
                                    )
                            }
                        }
                    }
//                    if (caseUseCases.checkCase(event.case.uid)) {
//                        caseUseCases.updateCase(event.case)
//                    }
                }
            }
        }
    }
}
