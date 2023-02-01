package com.omtorney.snapcase.domain.usecase

import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.domain.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentCases @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke(): Flow<List<Case>> {
        return repository.getRecentCases()
    }
}