package com.algolia.instantsearch.insights.internal.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.algolia.instantsearch.insights.internal.InsightsMap
import com.algolia.instantsearch.insights.internal.logging.InsightsLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class InsightsWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        InsightsLogger.log("Worker started with indices ${InsightsMap.keys} from work request $id.")
        val result = withContext(Dispatchers.IO) {
            val hasAnyEventFailed = InsightsMap
                .map { it.value.uploader.uploadAll().isEmpty() }
                .any { !it }
            if (hasAnyEventFailed) Result.retry() else Result.success()
        }
        InsightsLogger.log("Worker ended with result: $result.")
        return result
    }
}
