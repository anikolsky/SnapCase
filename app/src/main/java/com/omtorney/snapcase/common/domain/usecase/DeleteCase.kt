package com.omtorney.snapcase.common.domain.usecase

import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.domain.Repository
import javax.inject.Inject

class DeleteCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(case: Case) {
        repository.deleteFavorite(case)
    }
}