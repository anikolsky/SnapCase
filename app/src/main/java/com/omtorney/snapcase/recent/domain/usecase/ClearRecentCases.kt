package com.omtorney.snapcase.recent.domain.usecase

import com.omtorney.snapcase.common.domain.Repository
import javax.inject.Inject

class ClearRecentCases @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke() {
        repository.clearRecentCases()
    }
}