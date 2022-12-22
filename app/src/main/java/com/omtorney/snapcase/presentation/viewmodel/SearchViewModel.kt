package com.omtorney.snapcase.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.data.court.Courts
import com.omtorney.snapcase.data.model.Case
import com.omtorney.snapcase.domain.usecase.SearchCaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchCaseUseCase: SearchCaseUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _search = MutableStateFlow(listOf<Case>())
    val search = _search.asStateFlow()

    fun searchCase(input: String) = viewModelScope.launch {
        _isLoading.value = true
        _search.value = searchCaseUseCase.execute(Courts.Dmitrov, input)
        _isLoading.value = false
    }
}