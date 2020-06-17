package com.algolia.instantsearch.insights.internal.event

import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.insights.internal.InsightsLogger
import com.algolia.instantsearch.insights.internal.extension.uploadEvents
import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator

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
