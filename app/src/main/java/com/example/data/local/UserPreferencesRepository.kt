package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

  private val KEY_BOOKMARKS = stringSetPreferencesKey("bookmarked_service_ids")
  private val KEY_RECENT_SERVICES = stringPreferencesKey("recent_service_ids_csv")
  private val KEY_CHECKED_DOCS = stringSetPreferencesKey("checked_document_keys")
  private val KEY_CUSTOM_API_KEY = stringPreferencesKey("custom_gemini_api_key")

  val bookmarkedServiceIds: Flow<Set<String>> = context.dataStore.data
    .map { preferences ->
      preferences[KEY_BOOKMARKS] ?: emptySet()
    }

  val recentServiceIds: Flow<List<String>> = context.dataStore.data
    .map { preferences ->
      val csv = preferences[KEY_RECENT_SERVICES] ?: ""
      if (csv.isBlank()) emptyList() else csv.split(",").filter { it.isNotBlank() }
    }

  val checkedDocumentKeys: Flow<Set<String>> = context.dataStore.data
    .map { preferences ->
      preferences[KEY_CHECKED_DOCS] ?: emptySet()
    }

  val customGeminiApiKey: Flow<String> = context.dataStore.data
    .map { preferences ->
      preferences[KEY_CUSTOM_API_KEY] ?: ""
    }

  suspend fun setCustomApiKey(key: String) {
    context.dataStore.edit { preferences ->
      preferences[KEY_CUSTOM_API_KEY] = key.trim()
    }
  }

  suspend fun toggleBookmark(serviceId: String): Boolean {
    var isNowBookmarked = false
    context.dataStore.edit { preferences ->
      val current = preferences[KEY_BOOKMARKS] ?: emptySet()
      if (current.contains(serviceId)) {
        preferences[KEY_BOOKMARKS] = current - serviceId
        isNowBookmarked = false
      } else {
        preferences[KEY_BOOKMARKS] = current + serviceId
        isNowBookmarked = true
      }
    }
    return isNowBookmarked
  }

  suspend fun removeBookmark(serviceId: String) {
    context.dataStore.edit { preferences ->
      val current = preferences[KEY_BOOKMARKS] ?: emptySet()
      preferences[KEY_BOOKMARKS] = current - serviceId
    }
  }

  suspend fun addRecentService(serviceId: String) {
    context.dataStore.edit { preferences ->
      val csv = preferences[KEY_RECENT_SERVICES] ?: ""
      val list = if (csv.isBlank()) mutableListOf() else csv.split(",").filter { it.isNotBlank() }.toMutableList()
      list.remove(serviceId)
      list.add(0, serviceId)
      val trimmed = list.take(5)
      preferences[KEY_RECENT_SERVICES] = trimmed.joinToString(",")
    }
  }

  suspend fun toggleDocumentChecked(serviceId: String, docId: String): Boolean {
    val key = "$serviceId:$docId"
    var isNowChecked = false
    context.dataStore.edit { preferences ->
      val current = preferences[KEY_CHECKED_DOCS] ?: emptySet()
      if (current.contains(key)) {
        preferences[KEY_CHECKED_DOCS] = current - key
        isNowChecked = false
      } else {
        preferences[KEY_CHECKED_DOCS] = current + key
        isNowChecked = true
      }
    }
    return isNowChecked
  }

  suspend fun resetServiceChecklist(serviceId: String) {
    context.dataStore.edit { preferences ->
      val current = preferences[KEY_CHECKED_DOCS] ?: emptySet()
      val filtered = current.filterNot { it.startsWith("$serviceId:") }.toSet()
      preferences[KEY_CHECKED_DOCS] = filtered
    }
  }
}
