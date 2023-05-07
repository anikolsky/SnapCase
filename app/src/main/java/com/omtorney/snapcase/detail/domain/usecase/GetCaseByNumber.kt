package com.omtorney.snapcase.detail.domain.usecase

import com.omtorney.snapcase.common.domain.Repository
import com.omtorney.snapcase.common.domain.model.Case
import javax.inject.Inject

class GetCaseByNumber @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(number: String): Case? {
        return repository.getCaseByNumber(number)
    }
}