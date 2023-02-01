package com.omtorney.snapcase.domain.usecase

import com.omtorney.snapcase.domain.Repository
import com.omtorney.snapcase.domain.model.Case
import javax.inject.Inject

class GetCaseByNumber @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(number: String): Case? {
        return repository.getCaseByNumber(number)
    }
}