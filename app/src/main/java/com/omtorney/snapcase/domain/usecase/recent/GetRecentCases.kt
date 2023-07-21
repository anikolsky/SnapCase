package com.omtorney.snapcase.domain.usecase.recent

import com.omtorney.snapcase.domain.repository.Repository
import javax.inject.Inject

class GetRecentCases @Inject constructor(
    private val repository: Repository
) {
//    operator fun invoke(): Flow<List<Case>> {
//        return repository.getRecentCases()
//    }
}