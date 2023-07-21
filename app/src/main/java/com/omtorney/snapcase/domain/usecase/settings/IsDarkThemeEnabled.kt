package com.omtorney.snapcase.domain.usecase.settings

import com.omtorney.snapcase.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsDarkThemeEnabled @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.isDarkThemeEnabled
    }
}
