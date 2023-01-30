package com.omtorney.snapcase.presentation.detail

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val caseUseCases: CaseUseCases
) : ViewModel() {

    private val _state = mutableStateOf(DetailState())
    val state: State<DetailState> = _state

    fun fillCase(case: Case) = viewModelScope.launch(Dispatchers.IO) {
        kotlin.runCatching {
            _isLoading.value = true
            caseUseCases.fillCase(case, Courts.Dmitrov)
        }.fold(
            onSuccess = {
                _case.value = it
                if (caseUseCases.checkCase(it.uid)) caseUseCases.updateCase(it)
            },
            onFailure = { Log.d("DetailViewModel", it.message ?: "") }
        )
        _isLoading.value = false
    }

    fun saveCase(case: Case) = viewModelScope.launch(Dispatchers.IO) {
        kotlin.runCatching {
            caseUseCases.saveCase(case)
        }.fold(
            onSuccess = { _state.value = State.Success },
            onFailure = { _state.value = State.Failure(it.message ?: "") }
        )
    }
}