package com.algolia.instantsearch.insights.internal.worker

import com.algolia.instantsearch.insights.internal.data.settings.InsightsSettings
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import java.util.concurrent.TimeUnit
import kotlin.math.max

internal class WorkerAndroidJob(
    jobManager: JobManager,
    private val settings: InsightsSettings,
) : InsightsWorker {

    private var repeatIntervalInMinutes = defaultRepeatIntervalInMinutes

    init {
        jobManager.addJobCreator(EventJobCreator())
    }

    override fun setInterval(intervalInMinutes: Long) {
        repeatIntervalInMinutes = max(defaultRepeatIntervalInMinutes, intervalInMinutes)
    }

    override fun startPeriodicUpload() {
        if (settings.jobId == defaultJobId) {
            val jobId = JobRequest
                .Builder(EventJobCreator.Tag.Periodic.name)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setPeriodic(
                    TimeUnit.MINUTES.toMillis(repeatIntervalInMinutes),
                    TimeUnit.MINUTES.toMillis(flexTimeIntervalInMinutes)
                )
                .build()
                .schedule()
            settings.jobId = jobId
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
