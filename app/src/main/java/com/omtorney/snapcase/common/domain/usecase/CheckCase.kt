package com.omtorney.snapcase.common.domain.usecase

import com.omtorney.snapcase.common.domain.Repository
import javax.inject.Inject

class CheckCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(uid: String): Boolean {
        return repository.checkCase(uid) > 0
    }
}