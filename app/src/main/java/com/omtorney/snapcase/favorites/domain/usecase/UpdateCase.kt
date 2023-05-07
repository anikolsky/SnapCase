package com.omtorney.snapcase.favorites.domain.usecase

import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.domain.Repository
import javax.inject.Inject

class UpdateCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(case: Case) {
        repository.updateFavorite(case)
    }
}