package com.omtorney.snapcase.common.domain.usecase

import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.domain.Repository
import javax.inject.Inject

class SaveCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(case: Case) {
        repository.saveCase(case)
    }
}
