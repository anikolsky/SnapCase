package com.omtorney.snapcase.domain.usecase.favorites

import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteCases @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): Flow<List<Case>> {
        return repository.getFavoriteCases()
    }
}
