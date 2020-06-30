package com.algolia.instantsearch.insights.internal.extension

import android.content.Context
import android.content.SharedPreferences
import com.algolia.instantsearch.insights.internal.database.SharedPreferencesDelegate

internal var SharedPreferences.events: Set<String> by SharedPreferencesDelegate.StringSet(setOf())

internal fun Context.sharedPreferences(name: String, mode: Int = Context.MODE_PRIVATE): SharedPreferences {
    return getSharedPreferences(name, mode)
}
