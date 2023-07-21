package com.omtorney.snapcase.data.local

import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.presentation.screen.settings.CheckPeriod
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun saveCase(case: Case)
    suspend fun deleteCase(case: Case)
    suspend fun checkCase(uid: String): Int
    suspend fun getCaseByNumber(number: String): Case?
    fun getFavoriteCases(): Flow<List<Case>>

    val accentColor: Flow<Long>
    suspend fun setAccentColor(color: Long)
    val isDarkThemeEnabled: Flow<Boolean>
    suspend fun enableDarkTheme(enabled: Boolean)
    val selectedCourt: Flow<String>
    suspend fun setSelectedCourt(courtTitle: String)
    val caseCheckPeriod: Flow<CheckPeriod>
    suspend fun setCaseCheckPeriod(period: CheckPeriod)
}
