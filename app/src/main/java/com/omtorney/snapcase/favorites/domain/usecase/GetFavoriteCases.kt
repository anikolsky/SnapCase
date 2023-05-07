package com.omtorney.snapcase.favorites.domain.usecase

import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.domain.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteCases @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): Flow<List<Case>> {
        return repository.getFavoriteCases()
    }
}