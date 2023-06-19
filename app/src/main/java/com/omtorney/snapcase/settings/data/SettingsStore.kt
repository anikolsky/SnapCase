package com.omtorney.snapcase.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.omtorney.snapcase.common.domain.court.Courts
import com.omtorney.snapcase.common.util.Constants
import com.omtorney.snapcase.settings.presentation.CheckPeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val ACCENT_COLOR = longPreferencesKey("accent_color")
        val SELECTED_COURT = stringPreferencesKey("selected_court")
        val BACKGROUND_CHECK_PERIOD_KEY = intPreferencesKey("background_check_period")
    }

    val getAccentColor: Flow<Long> = dataStore.data
        .map { it[ACCENT_COLOR] ?: Constants.INITIAL_COLOR }

    suspend fun setAccentColor(color: Long) {
        dataStore.edit { it[ACCENT_COLOR] = color }
    }

    val getSelectedCourt: Flow<String> = dataStore.data
        .map { it[SELECTED_COURT] ?: "Дмитровский городской" }

    suspend fun setSelectedCourt(courtTitle: String) {
        dataStore.edit { it[SELECTED_COURT] = courtTitle }
    }

    val getCaseCheckPeriod: Flow<CheckPeriod> = dataStore.data
        .map { it[BACKGROUND_CHECK_PERIOD_KEY] ?: CheckPeriod.ONE_HOUR.ordinal }
        .map { CheckPeriod.values()[it] }

    suspend fun setCaseCheckPeriod(period: CheckPeriod) {
        dataStore.edit { it[BACKGROUND_CHECK_PERIOD_KEY] = period.ordinal }
    }
}
