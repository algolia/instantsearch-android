
package com.algolia.instantsearch.insights.internal.uploader

import com.algolia.instantsearch.insights.internal.data.distant.InsightsDistantRepository
import com.algolia.instantsearch.insights.internal.data.local.InsightsLocalRepository
import com.algolia.instantsearch.insights.internal.event.EventResponse
import com.algolia.instantsearch.insights.internal.extension.currentTimeMillis
import com.algolia.instantsearch.insights.internal.logging.InsightsLogger

internal class InsightsEventUploader(
    private val localRepository: InsightsLocalRepository,
    private val distantRepository: InsightsDistantRepository,
) : InsightsUploader {

    override val applicationID: String get() = distantRepository.applicationID
    override val apiKey: String get() = distantRepository.apiKey

    override suspend fun uploadAll(): List<EventResponse> {
        val events = localRepository.read()
        InsightsLogger.log("Flushing remaining ${events.size} events.")
        val recentEvents = events.filter { insightsEvent ->
            insightsEvent.timestamp?.let { !isExpired(it) } ?: true
        }
        val failedEvents = sendEvents(recentEvents).filterEventsWhenException()
        localRepository.overwrite(failedEvents.map { it.event })
        return failedEvents
    }

    private suspend fun sendEvents(events: List<com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO>): List<EventResponse> {
        return events.mapNotNull { event ->
            distantRepository.send(event)
        }
    }

    private fun List<EventResponse>.filterEventsWhenException(): List<EventResponse> {
        return this.filter { it.code == -1 }
    }

    private fun isExpired(timestampMillis: Long): Boolean {
        return currentTimeMillis - timestampMillis >= fourDaysMillis
    }

    companion object {
        const val fourDaysMillis = 4 * 24 * 60 * 60 * 1000 // 4 days in millis
    }
}
