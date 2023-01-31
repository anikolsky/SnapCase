package com.omtorney.snapcase.presentation.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.domain.usecase.CaseUseCases
import com.omtorney.snapcase.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val caseUseCases: CaseUseCases
) : ViewModel() {

    private val _state = mutableStateOf(DetailState())
    val state: State<DetailState> = _state

    init {
        // caseParam
    }

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.Fill -> {
                viewModelScope.launch {
                    caseUseCases.fillCase(event.case, Courts.Dmitrov).collect { result ->
                        when (result) {
                            is Resource.Loading -> {
                                _state.value = DetailState(isLoading = true)
                            }
                            is Resource.Success -> {
                                _state.value = DetailState(case = result.data)
                            }
                            is Resource.Error -> {
                                _state.value = DetailState(error = result.message ?: "Unexpected error")
                            }
                        }
                    }
//                    if (caseUseCases.checkCase(event.case.uid)) {
//                        caseUseCases.updateCase(event.case)
//                    }
                }
            }
            is DetailEvent.Save -> {
                viewModelScope.launch {
                    caseUseCases.saveCase(event.case)
                }
            }
        }
    }
}