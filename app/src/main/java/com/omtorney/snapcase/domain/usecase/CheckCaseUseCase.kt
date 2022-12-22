package com.omtorney.snapcase.domain.usecase

import com.omtorney.snapcase.domain.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckCaseUseCase @Inject constructor(private val repository: Repository) {

    suspend fun execute(uid: String): Boolean {
        return withContext(Dispatchers.IO) {
            repository.checkCase(uid) > 0
        }
    }
}