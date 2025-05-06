package com.vp.era_test_pexels.control

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.dataStore by preferencesDataStore(name = "search_prefs")

class SearchHistoryManager(private val context: Context) {

    private val SEARCH_HISTORY_KEY = stringSetPreferencesKey("search_history")

    // Save search history to store
    fun saveSearchQuery(query: String) {
        runBlocking {
            context.dataStore.edit { preferences ->
                val historySet = preferences[SEARCH_HISTORY_KEY] ?: emptySet()
                preferences[SEARCH_HISTORY_KEY] = historySet + query
            }
        }
    }

    // Get search history from store
    fun getSearchHistory(): List<String> {
        return runBlocking {
            val preferences = context.dataStore.data.first()
            preferences[SEARCH_HISTORY_KEY]?.toList() ?: emptyList()
        }
    }
}
