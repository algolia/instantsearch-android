package com.algolia.instantsearch.insights

import android.content.Context
import android.content.SharedPreferences


internal const val Type = "type"

internal var SharedPreferences.events by SharedPreferencesDelegate.StringSet(setOf())

internal fun Context.sharedPreferences(name: String, mode: Int = Context.MODE_PRIVATE): SharedPreferences {
    return getSharedPreferences(name, mode)
}
