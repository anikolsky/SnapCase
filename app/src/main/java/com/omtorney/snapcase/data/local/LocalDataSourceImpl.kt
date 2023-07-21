package com.omtorney.snapcase.data.local

import com.omtorney.snapcase.data.local.database.CaseDao
import com.omtorney.snapcase.data.local.datastore.SettingsStore
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.presentation.screen.settings.CheckPeriod
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val caseDao: CaseDao,
    private val settingsStore: SettingsStore
) : LocalDataSource {

    override suspend fun saveCase(case: Case) = caseDao.upsert(case)
    override suspend fun deleteCase(case: Case) = caseDao.delete(case)
    override suspend fun checkCase(uid: String): Int = caseDao.checkCase(uid)
    override suspend fun getCaseByNumber(number: String): Case? = caseDao.getCaseByNumber(number)
    override fun getFavoriteCases(): Flow<List<Case>> = caseDao.getFavoriteCases()

    override val accentColor: Flow<Long> = settingsStore.accentColor
    override suspend fun setAccentColor(color: Long) = settingsStore.setAccentColor(color)
    override val isDarkThemeEnabled: Flow<Boolean> = settingsStore.isDarkThemeEnabled
    override suspend fun enableDarkTheme(enabled: Boolean) = settingsStore.enableDarkTheme(enabled)
    override val selectedCourt: Flow<String> = settingsStore.selectedCourt
    override suspend fun setSelectedCourt(courtTitle: String) = settingsStore.setSelectedCourt(courtTitle)
    override val caseCheckPeriod: Flow<CheckPeriod> = settingsStore.caseCheckPeriod
    override suspend fun setCaseCheckPeriod(period: CheckPeriod) = settingsStore.setCaseCheckPeriod(period)
}
