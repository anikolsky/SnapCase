package com.omtorney.snapcase.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val ACCENT_COLOR = longPreferencesKey("accent_color")
        val SELECTED_COURT = stringPreferencesKey("selected_court")
    }

    val getAccentColor: Flow<Long> = dataStore.data.map { preferences ->
            preferences[ACCENT_COLOR] ?: Constants.INITIAL_COLOR
        }

    suspend fun setAccentColor(color: Long) {
        dataStore.edit { preferences ->
            preferences[ACCENT_COLOR] = color
        }
    }

    val getSelectedCourt: Flow<String> = dataStore.data.map { preferences ->
            preferences[SELECTED_COURT] ?: Courts.Dmitrov.title
        }

    suspend fun setSelectedCourt(courtTitle: String) {
        dataStore.edit { preferences ->
            preferences[SELECTED_COURT] = courtTitle
        }
    }
}