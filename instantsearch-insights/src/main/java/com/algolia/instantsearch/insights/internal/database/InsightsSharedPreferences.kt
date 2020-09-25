package com.algolia.instantsearch.insights.internal.database

import android.content.Context
import android.content.SharedPreferences
import com.algolia.instantsearch.insights.internal.event.EventUploaderAndroidJob

internal class InsightsSharedPreferences(context: Context) {

    private val preferences = context.getSharedPreferences(INSIGHTS_SHARED_PREFS, Context.MODE_PRIVATE)
    private var SharedPreferences.jobId by SharedPreferencesDelegate.Int(EventUploaderAndroidJob.defaultJobId)
    private var SharedPreferences.userToken by SharedPreferencesDelegate.String()

    internal var jobId: Int
        get() = preferences.jobId
        set(value) {
            preferences.jobId = value
        }

    internal var userToken: String?
        get() = preferences.userToken
        set(value) {
            value?.let { preferences.userToken = it }
        }

    companion object {
        private const val INSIGHTS_SHARED_PREFS = "InsightsEvents"
    }
}
