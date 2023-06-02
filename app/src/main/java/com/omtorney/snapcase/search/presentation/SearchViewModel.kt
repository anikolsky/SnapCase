package com.omtorney.snapcase.search.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.common.domain.court.CaseType
import com.omtorney.snapcase.common.domain.usecase.CaseUseCases
import com.omtorney.snapcase.common.presentation.logd
import com.omtorney.snapcase.common.util.Resource
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
        val caseTypeInput = savedStateHandle.get<String>("caseType")
        val courtTitle = savedStateHandle.get<String>("courtTitle")!!
        val searchInput = savedStateHandle.get<String>("searchInput")!!
        val caseType = when (caseTypeInput) {
            CaseType.GPK.title -> CaseType.GPK
            else -> CaseType.KAS
        }
        searchCase(courtTitle, caseType, searchInput)
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.CacheCase -> {
                viewModelScope.launch {
                    caseUseCases.saveCase(event.case)
                }
            }
        }
    }

    private fun searchCase(courtTitle: String, caseType: CaseType, input: String) {
        viewModelScope.launch {
            caseUseCases.searchCase(courtTitle, caseType, input).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = SearchState(isLoading = true)
                    }
                    is Resource.Success -> {
                        _state.value = SearchState(cases = result.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        _state.value = SearchState(error = result.message ?: "Unexpected error")
                        logd("error: ${result.message}")
                    }
                }
            }
        }
    }
}