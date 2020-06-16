package com.algolia.instantsearch.insights.internal.event

import android.content.Context
import android.content.SharedPreferences
import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.insights.event.EventUploader
import com.algolia.instantsearch.insights.internal.InsightsLogger
import com.algolia.instantsearch.insights.internal.database.SharedPreferencesDelegate
import com.algolia.instantsearch.insights.internal.webservice.uploadEvents
import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import java.util.concurrent.TimeUnit
import kotlin.math.max


internal class EventUploaderAndroidJob(context: Context) :
    EventUploader {
    private val preferences = context.getSharedPreferences("Insights", Context.MODE_PRIVATE)
    private var SharedPreferences.jobId by SharedPreferencesDelegate.Int(defaultJobId)
    private var repeatIntervalInMinutes =
        defaultRepeatIntervalInMinutes

    companion object {
        private const val defaultRepeatIntervalInMinutes = 15L
        private const val flexTimeIntervalInMinutes = 5L
        private const val defaultJobId = -1
    }

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
}

internal class EventJobCreator : JobCreator {

    enum class Tag {
        OneTime,
        Periodic,
    }

    override fun create(tag: String): Job? {
        return when (Tag.valueOf(tag)) {
            Tag.OneTime, Tag.Periodic -> object : Job() {
                override fun onRunJob(params: Params): Result {
                    InsightsLogger.log("Worker started with indices ${Insights.insightsMap.keys}.")
                    val hasAnyEventFailed = Insights.insightsMap
                        .map { it.value.webService.uploadEvents(it.value.database, it.key).isEmpty() }
                        .any { !it }
                    val result = if (hasAnyEventFailed) Job.Result.FAILURE else Job.Result.SUCCESS
                    InsightsLogger.log("Worker ended with result: $result.")
                    return result
                }

            }
        }
    }
}
