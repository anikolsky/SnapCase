package com.omtorney.snapcase.domain.usecase.settings

import com.omtorney.snapcase.domain.repository.Repository
import com.omtorney.snapcase.presentation.screen.settings.CheckPeriod
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCaseCheckPeriod @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): Flow<CheckPeriod> {
        return repository.caseCheckPeriod
    }
}
