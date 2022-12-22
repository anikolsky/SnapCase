package com.omtorney.snapcase.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.data.State
import com.omtorney.snapcase.data.court.Courts
import com.omtorney.snapcase.data.model.Case
import com.omtorney.snapcase.domain.usecase.CheckCaseUseCase
import com.omtorney.snapcase.domain.usecase.FillCaseUseCase
import com.omtorney.snapcase.domain.usecase.SaveCaseUseCase
import com.omtorney.snapcase.domain.usecase.UpdateCaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val fillCaseUseCase: FillCaseUseCase,
    private val saveCaseUseCase: SaveCaseUseCase,
    private val checkCaseUseCase: CheckCaseUseCase,
    private val updateCaseUseCase: UpdateCaseUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _state = MutableStateFlow<State>(State.Default)
    val state = _state.asStateFlow()

    private val _case = MutableStateFlow(Case())
    val case = _case.asStateFlow()

    fun fillCase(case: Case) = viewModelScope.launch(Dispatchers.IO) {
        kotlin.runCatching {
            _isLoading.value = true
            fillCaseUseCase.execute(case, Courts.Dmitrov)
        }.fold(
            onSuccess = {
                _case.value = it
                if (checkCaseUseCase.execute(it.uid)) updateCaseUseCase.execute(it)
            },
            onFailure = { Log.d("DetailViewModel", it.message ?: "") }
        )
        _isLoading.value = false
    }

    fun saveCase(case: Case) = viewModelScope.launch(Dispatchers.IO) {
        kotlin.runCatching {
            saveCaseUseCase.execute(case)
        }.fold(
            onSuccess = { _state.value = State.Success },
            onFailure = { _state.value = State.Failure(it.message ?: "") }
        )
    }
}