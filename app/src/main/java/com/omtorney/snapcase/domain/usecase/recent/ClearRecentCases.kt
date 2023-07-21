package com.omtorney.snapcase.domain.usecase.recent

import com.omtorney.snapcase.domain.repository.Repository
import javax.inject.Inject

class ClearRecentCases @Inject constructor(
    private val repository: Repository
) {
//    suspend operator fun invoke() {
//        repository.clearRecentCases()
//    }
}