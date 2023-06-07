package com.omtorney.snapcase.recent.domain.usecase

import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.domain.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentCases @Inject constructor(
    private val repository: Repository
) {
//    operator fun invoke(): Flow<List<Case>> {
//        return repository.getRecentCases()
//    }
}