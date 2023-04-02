package com.omtorney.snapcase.presentation.search

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.domain.court.CaseType
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
        val caseTypeInput = savedStateHandle.get<String>("caseType")
        val courtTitle = savedStateHandle.get<String>("courtTitle")!!
        val searchInput = savedStateHandle.get<String>("searchInput")!!
        val caseType = when (caseTypeInput) {
            CaseType.GPK.title -> CaseType.GPK
            else -> CaseType.KAS
        }
        Log.d("TESTLOG", "SearchViewModel:\ncourtTitle: $courtTitle\ncaseType: $caseType\nsearchInput: $searchInput")
        searchCase(courtTitle, caseType, searchInput)
    }

    fun cacheCase(case: Case) {
        viewModelScope.launch {
            caseUseCases.saveCase(case)
        }
    }

    private fun searchCase(courtTitle: String, caseType: CaseType, input: String) {
        viewModelScope.launch {
            caseUseCases.searchCase(courtTitle, caseType, input).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = SearchState(isLoading = true)
                        Log.d("TESTLOG", "SearchViewModel: searchCase: loading...")
                    }
                    is Resource.Success -> {
                        _state.value = SearchState(cases = result.data ?: emptyList())
                        Log.d("TESTLOG", "SearchViewModel: searchCase: success: result.data: ${result.data}")
                    }
                    is Resource.Error -> {
                        _state.value = SearchState(error = result.message ?: "Unexpected error")
                        Log.d("TESTLOG", "SearchViewModel: searchCase: error: ${result.message}")
                    }
                }
            }
        }
    }
}