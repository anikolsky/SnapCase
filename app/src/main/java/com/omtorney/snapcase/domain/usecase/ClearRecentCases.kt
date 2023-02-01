package com.omtorney.snapcase.domain.usecase

import com.omtorney.snapcase.domain.Repository
import javax.inject.Inject

class ClearRecentCases @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke() {
        repository.clearRecentCases()
    }
}