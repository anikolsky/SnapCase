package com.omtorney.snapcase.domain.usecase.settings

import com.omtorney.snapcase.domain.repository.Repository
import com.omtorney.snapcase.presentation.screen.settings.CheckPeriod
import javax.inject.Inject

class SetCaseCheckPeriod @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(period: CheckPeriod) {
        repository.setCaseCheckPeriod(period)
    }
}
