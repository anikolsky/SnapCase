package com.omtorney.snapcase.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsStore @Inject constructor(
    private val context: Context
) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("SnapCaseSettings")
        val ACCENT_COLOR = longPreferencesKey("accent_color")
        val SELECTED_COURT = stringPreferencesKey("selected_court")
    }

    val getAccentColor: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[ACCENT_COLOR] ?: Constants.INITIAL_COLOR
        }

    suspend fun setAccentColor(color: Long) {
        context.dataStore.edit { preferences ->
            preferences[ACCENT_COLOR] = color
        }
    }

    val getSelectedCourt: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_COURT] ?: Courts.Dmitrov.title
        }

    suspend fun setSelectedCourt(courtTitle: String) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_COURT] = courtTitle
        }
    }
}