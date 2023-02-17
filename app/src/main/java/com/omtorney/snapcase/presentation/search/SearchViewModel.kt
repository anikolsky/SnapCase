package com.omtorney.snapcase.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.domain.usecase.CaseUseCases
import com.omtorney.snapcase.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val caseUseCases: CaseUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(SearchState())
    val state: State<SearchState> = _state

    init {
        savedStateHandle.get<String>("searchInput")?.let { input ->
            searchCase(input)
        }
    }

    fun cacheCase(case: Case) {
        viewModelScope.launch {
            caseUseCases.saveCase(case)
        }
    }

    private fun searchCase(input: String) = viewModelScope.launch {
        caseUseCases.searchCase(Courts.Dmitrov, input).collect { result ->
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
        }
    }
}