package com.algolia.instantsearch.insights.internal.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.algolia.instantsearch.insights.internal.InsightsMap
import com.algolia.instantsearch.insights.internal.logging.InsightsLogger

internal class InsightsWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        InsightsLogger.log("Worker started with indices ${InsightsMap.keys}.")
        val hasAnyEventFailed = InsightsMap
            .map { it.value.uploader.uploadAll().isEmpty() }
            .any { !it }
        val result = if (hasAnyEventFailed) Result.retry() else Result.success()
        InsightsLogger.log("Worker ended with result: $result.")
        return result
    }
}
