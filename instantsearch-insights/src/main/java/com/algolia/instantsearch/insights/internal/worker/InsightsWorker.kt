package com.algolia.instantsearch.insights.internal.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.insights.internal.logging.InsightsLogger

internal class InsightsWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        InsightsLogger.log("Worker started with indices ${Insights.insightsMap.keys}.")
        val hasAnyEventFailed = Insights.insightsMap
            .map { it.value.uploader.uploadAll().isEmpty() }
            .any { !it }
        val result = if (hasAnyEventFailed) Result.retry() else Result.success()
        InsightsLogger.log("Worker ended with result: $result.")
        return result
    }
}
