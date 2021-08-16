package com.algolia.instantsearch.insights.internal.data.settings

import android.content.SharedPreferences
import com.algolia.instantsearch.insights.internal.extension.SharedPreferencesDelegate

internal class InsightsEventSettings(
    private val preferences: SharedPreferences,
) : InsightsSettings {

    private var SharedPreferences.jobId by SharedPreferencesDelegate.String()
    private var SharedPreferences.userToken by SharedPreferencesDelegate.String()

    override var workId: String?
        get() = preferences.jobId
        set(value) {
            preferences.jobId = value.toString()
        }

    override var userToken: String?
        get() = preferences.userToken
        set(value) {
            value?.let { preferences.userToken = it }
        }
}
