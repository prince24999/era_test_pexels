package com.vp.era_test_pexels.control

import android.content.Context
import androidx.core.content.edit

class SearchHistoryManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)

    // Hàm lưu lịch sử tìm kiếm
    fun saveSearchQuery(query: String) {
        val historyList = sharedPreferences.getStringSet("searchHistory", emptySet())?.toMutableSet() ?: mutableSetOf()
        historyList.add(query)

        sharedPreferences.edit() { putStringSet("searchHistory", historyList) }
    }

    // Hàm lấy danh sách lịch sử tìm kiếm
    fun getSearchHistory(): List<String> {
        return sharedPreferences.getStringSet("searchHistory", emptySet())?.toList() ?: listOf()
    }
}

