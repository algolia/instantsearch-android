package com.algolia.instantsearch.insights.internal.event

import android.content.Context
import com.algolia.instantsearch.insights.internal.database.InsightsSharedPreferences
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import java.util.concurrent.TimeUnit
import kotlin.math.max

internal class EventUploaderAndroidJob(
    context: Context,
    private val preferences: InsightsSharedPreferences,
) : EventUploader {

    private var repeatIntervalInMinutes = defaultRepeatIntervalInMinutes

    init {
        JobManager.create(context).addJobCreator(EventJobCreator())
    }

    override fun setInterval(intervalInMinutes: Long) {
        repeatIntervalInMinutes = max(defaultRepeatIntervalInMinutes, intervalInMinutes)
    }

    override fun startPeriodicUpload() {
        if (preferences.jobId == defaultJobId) {
            val jobId = JobRequest
                .Builder(EventJobCreator.Tag.Periodic.name)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setPeriodic(
                    TimeUnit.MINUTES.toMillis(repeatIntervalInMinutes),
                    TimeUnit.MINUTES.toMillis(flexTimeIntervalInMinutes)
                )
                .build()
                .schedule()
            preferences.jobId = jobId
        }
    }

    override fun startOneTimeUpload() {
        JobRequest
            .Builder(EventJobCreator.Tag.OneTime.name)
            .startNow()
            .setUpdateCurrent(true)
            .build()
            .schedule()
    }

    companion object {
        private const val defaultRepeatIntervalInMinutes = 15L
        private const val flexTimeIntervalInMinutes = 5L
        internal const val defaultJobId = -1
    }
}
