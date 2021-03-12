package com.algolia.instantsearch.insights.internal.worker

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.algolia.instantsearch.insights.internal.data.settings.InsightsSettings
import java.util.concurrent.TimeUnit
import kotlin.math.max

internal class InsightsWorkManager(
    private val workManager: WorkManager,
    private val settings: InsightsSettings,
) : InsightsManager {

    private var repeatIntervalInMinutes: Long = DEFAULT_REPEAT_INTERVAL_IN_MINUTES

    override fun setInterval(intervalInMinutes: Long) {
        repeatIntervalInMinutes = max(DEFAULT_REPEAT_INTERVAL_IN_MINUTES, intervalInMinutes)
    }

    override fun startPeriodicUpload() {
        if (settings.workId != null) return
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = PeriodicWorkRequestBuilder<InsightsWorker>(
            repeatInterval = repeatIntervalInMinutes, repeatIntervalTimeUnit = TimeUnit.MINUTES,
            flexTimeInterval = FLEX_TIME_INTERVAL_IN_MINUTES, flexTimeIntervalUnit = TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniquePeriodicWork(WORK_NAME_PERIODIC, ExistingPeriodicWorkPolicy.KEEP, workRequest)
        settings.workId = workRequest.id
    }

    override fun startOneTimeUpload() {
        val workRequest = OneTimeWorkRequest.from(InsightsWorker::class.java)
        workManager.enqueueUniqueWork(WORK_NAME_ONETIME, ExistingWorkPolicy.REPLACE, workRequest)
    }

    companion object {
        private const val DEFAULT_REPEAT_INTERVAL_IN_MINUTES = 15L
        private const val FLEX_TIME_INTERVAL_IN_MINUTES = 5L
        private const val WORK_NAME_PERIODIC = "PERIODIC_UPLOAD"
        private const val WORK_NAME_ONETIME = "ONETIME_UPLOAD"
    }
}
