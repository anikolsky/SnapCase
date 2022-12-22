package com.omtorney.snapcase.domain.usecase

import com.omtorney.snapcase.data.model.Case
import com.omtorney.snapcase.domain.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveCaseUseCase @Inject constructor(private val repository: Repository) {

    suspend fun execute(case: Case) {
        withContext(Dispatchers.IO) {
            repository.addFavorite(case)
        }
    }
}