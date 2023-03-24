package com.omtorney.snapcase.domain.usecase.settings

import com.omtorney.snapcase.domain.Repository
import kotlinx.coroutines.flow.Flow

class GetSelectedCourt(
    private val repository: Repository
) {
    operator fun invoke(): Flow<String> {
        return repository.getSelectedCourt
    }
}