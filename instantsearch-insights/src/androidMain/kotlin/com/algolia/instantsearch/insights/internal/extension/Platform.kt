package com.algolia.instantsearch.insights.internal.extension

import android.content.Context
import android.content.SharedPreferences
import com.algolia.instantsearch.migration2to3.IndexName

internal var SharedPreferences.events: Set<String> by SharedPreferencesDelegate.StringSet(setOf())

internal fun Context.sharedPreferences(name: String, mode: Int = Context.MODE_PRIVATE): SharedPreferences {
    return getSharedPreferences(name, mode)
}

internal fun Context.insightsSharedPreferences(indexName: IndexName) = sharedPreferences("Algolia Insights-$indexName")

/**
 * Get Insights Settings Shared Preferences.
 */
internal fun Context.insightsSettingsPreferences(): SharedPreferences {
    return getSharedPreferences("InsightsEvents", Context.MODE_PRIVATE)
}
