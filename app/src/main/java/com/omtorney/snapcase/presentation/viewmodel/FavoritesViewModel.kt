package com.omtorney.snapcase.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.data.court.Courts
import com.omtorney.snapcase.data.database.CaseDao
import com.omtorney.snapcase.data.model.Case
import com.omtorney.snapcase.domain.usecase.DeleteCaseUseCase
import com.omtorney.snapcase.domain.usecase.FillCaseUseCase
import com.omtorney.snapcase.domain.usecase.UpdateCaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val caseDao: CaseDao,
    private val deleteCaseUseCase: DeleteCaseUseCase,
    private val fillCaseUseCase: FillCaseUseCase,
    private val updateCaseUseCase: UpdateCaseUseCase
) : ViewModel() {

    val allCases = this.caseDao.getAll()
        .stateIn(
            scope = viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteCase(case: Case) = viewModelScope.launch {
        deleteCaseUseCase.execute(case)
    }

    fun refreshCases(cases: List<Case>) = viewModelScope.launch {
        cases.forEach { case ->
            fillCaseUseCase.execute(case, Courts.Dmitrov)
            updateCaseUseCase.execute(case)
        }
    }
}