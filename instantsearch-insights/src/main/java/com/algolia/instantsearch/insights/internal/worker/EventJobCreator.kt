package com.algolia.instantsearch.insights.internal.worker

import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.insights.internal.logging.InsightsLogger
import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator

internal class EventJobCreator : JobCreator {

    override fun create(tag: String): Job? {
        return when (Tag.valueOf(tag)) {
            Tag.OneTime, Tag.Periodic -> object : Job() {
                override fun onRunJob(params: Params): Result {
                    InsightsLogger.log("Worker started with indices ${Insights.insightsMap.keys}.")
                    val hasAnyEventFailed = Insights.insightsMap
                        .map { it.value.uploader.uploadAll().isEmpty() }
                        .any { !it }
                    val result = if (hasAnyEventFailed) Result.FAILURE else Job.Result.SUCCESS
                    InsightsLogger.log("Worker ended with result: $result.")
                    return result
                }
            }
        }
    }

    enum class Tag {
        OneTime,
        Periodic,
    }
}
