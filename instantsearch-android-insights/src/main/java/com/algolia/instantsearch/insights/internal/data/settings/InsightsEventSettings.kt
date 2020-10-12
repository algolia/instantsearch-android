package com.algolia.instantsearch.insights.internal.data.settings

import android.content.SharedPreferences
import com.algolia.instantsearch.insights.internal.extension.SharedPreferencesDelegate
import java.util.UUID

internal class InsightsEventSettings(
    private val preferences: SharedPreferences,
) : InsightsSettings {

    private var SharedPreferences.jobId by SharedPreferencesDelegate.String()
    private var SharedPreferences.userToken by SharedPreferencesDelegate.String()

    override var workId: UUID?
        get() = preferences.jobId?.let { UUID.fromString(it) }
        set(value) {
            preferences.jobId = value.toString()
        }

    override var userToken: String?
        get() = preferences.userToken
        set(value) {
            value?.let { preferences.userToken = it }
        }
}
