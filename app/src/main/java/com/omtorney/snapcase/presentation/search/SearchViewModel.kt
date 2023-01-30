package com.omtorney.snapcase.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.domain.usecase.CaseUseCases
import com.omtorney.snapcase.domain.usecase.SearchCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val caseUseCases: CaseUseCases
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _search = MutableStateFlow(listOf<Case>())
    val search = _search.asStateFlow()

    fun searchCase(input: String) = viewModelScope.launch {
        _isLoading.value = true
        _search.value = caseUseCases.searchCase(Courts.Dmitrov, input)
        _isLoading.value = false
    }
}