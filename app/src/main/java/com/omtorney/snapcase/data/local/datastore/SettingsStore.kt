package com.omtorney.snapcase.data.local.datastore

import android.content.res.Configuration
import android.content.res.Resources
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.omtorney.snapcase.util.Constants
import com.omtorney.snapcase.presentation.screen.settings.CheckPeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val ACCENT_COLOR = longPreferencesKey("accent_color")
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val SELECTED_COURT = stringPreferencesKey("selected_court")
        val BACKGROUND_CHECK_PERIOD_KEY = intPreferencesKey("background_check_period")
    }

    val accentColor: Flow<Long> = dataStore.data
        .map { it[ACCENT_COLOR] ?: Constants.INITIAL_COLOR }

    suspend fun setAccentColor(color: Long) {
        dataStore.edit { it[ACCENT_COLOR] = color }
    }

    private val systemTheme =
        when (Resources.getSystem().configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> { true }
            Configuration.UI_MODE_NIGHT_NO -> { false }
            else -> { false }
        }

    val isDarkThemeEnabled: Flow<Boolean> = dataStore.data
        .map { it[DARK_THEME] ?: systemTheme }

    suspend fun enableDarkTheme(enabled: Boolean) {
        dataStore.edit { it[DARK_THEME] = enabled }
    }

    val selectedCourt: Flow<String> = dataStore.data
        .map { it[SELECTED_COURT] ?: "Дмитровский городской" }

    suspend fun setSelectedCourt(courtTitle: String) {
        dataStore.edit { it[SELECTED_COURT] = courtTitle }
    }

    val caseCheckPeriod: Flow<CheckPeriod> = dataStore.data
        .map { it[BACKGROUND_CHECK_PERIOD_KEY] ?: CheckPeriod.ONE_HOUR.ordinal }
        .map { CheckPeriod.values()[it] }

    suspend fun setCaseCheckPeriod(period: CheckPeriod) {
        dataStore.edit { it[BACKGROUND_CHECK_PERIOD_KEY] = period.ordinal }
    }
}
