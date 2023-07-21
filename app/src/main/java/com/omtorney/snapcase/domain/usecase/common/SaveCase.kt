package com.omtorney.snapcase.domain.usecase.common

import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.domain.repository.Repository
import javax.inject.Inject

class SaveCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(case: Case) {
        repository.saveCase(case)
    }
}
