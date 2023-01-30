package com.omtorney.snapcase.presentation.act

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.domain.usecase.CaseUseCases
import com.omtorney.snapcase.domain.usecase.LoadActText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActViewModel @Inject constructor(
    private val caseUseCases: CaseUseCases
) : ViewModel() {

    private val _actLines = MutableStateFlow(arrayListOf<String>())
    val actLines = _actLines.asStateFlow()

    fun loadAct(url: String) = viewModelScope.launch {
        _actLines.value = caseUseCases.loadActText(Courts.Dmitrov, url)
    }
}