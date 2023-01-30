package com.omtorney.snapcase.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.data.database.CaseDao
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.domain.usecase.CaseUseCases
import com.omtorney.snapcase.domain.usecase.DeleteCase
import com.omtorney.snapcase.domain.usecase.FillCase
import com.omtorney.snapcase.domain.usecase.UpdateCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val caseDao: CaseDao,
    private val caseUseCases: CaseUseCases
) : ViewModel() {

    val allCases = this.caseDao.getAll()
        .stateIn(
            scope = viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteCase(case: Case) = viewModelScope.launch {
        caseUseCases.deleteCase(case)
    }

    fun refreshCases(cases: List<Case>) = viewModelScope.launch {
        cases.forEach { case ->
            caseUseCases.fillCase(case, Courts.Dmitrov)
            caseUseCases.updateCase(case)
        }
    }
}