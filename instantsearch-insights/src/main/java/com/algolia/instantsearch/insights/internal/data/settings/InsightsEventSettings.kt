package com.algolia.instantsearch.insights.internal.data.settings

import android.content.SharedPreferences
import com.algolia.instantsearch.insights.internal.extension.SharedPreferencesDelegate
import com.algolia.instantsearch.insights.internal.worker.WorkerAndroidJob

internal class InsightsEventSettings(
    private val preferences: SharedPreferences,
) : InsightsSettings {

    private var SharedPreferences.jobId by SharedPreferencesDelegate.Int(WorkerAndroidJob.defaultJobId)
    private var SharedPreferences.userToken by SharedPreferencesDelegate.String()

    override var jobId: Int
        get() = preferences.jobId
        set(value) {
            preferences.jobId = value
        }

    override var userToken: String?
        get() = preferences.userToken
        set(value) {
            value?.let { preferences.userToken = it }
        }
}
