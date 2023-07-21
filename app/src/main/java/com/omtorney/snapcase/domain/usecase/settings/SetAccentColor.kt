package com.omtorney.snapcase.domain.usecase.settings

import com.omtorney.snapcase.domain.repository.Repository
import javax.inject.Inject

class SetAccentColor @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(color: Long) {
        repository.setAccentColor(color)
    }
}
