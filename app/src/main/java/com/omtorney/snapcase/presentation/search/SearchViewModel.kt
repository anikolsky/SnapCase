package com.omtorney.snapcase.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.domain.usecase.CaseUseCases
import com.omtorney.snapcase.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val caseUseCases: CaseUseCases
) : ViewModel() {

    private val _state = mutableStateOf(SearchState())
    val state: State<SearchState> = _state

    fun searchCase(input: String) = viewModelScope.launch {
        caseUseCases.searchCase(Courts.Dmitrov, input).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = SearchState(isLoading = true)
                }
                is Resource.Success -> {
                    _state.value = SearchState(cases = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = SearchState(error = result.message ?: "Unexpected error")
                }
            }
        } // .launchIn(viewModelScope)
    }
}