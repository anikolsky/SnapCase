package com.omtorney.snapcase.common.domain.usecase

import com.omtorney.snapcase.common.domain.Repository
import javax.inject.Inject

class CheckCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(number: String): Boolean {
        return repository.checkCase(number) > 0
    }
}