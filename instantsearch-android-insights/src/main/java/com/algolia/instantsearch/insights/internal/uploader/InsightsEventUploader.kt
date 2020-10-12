package com.algolia.instantsearch.insights.internal.uploader

import com.algolia.instantsearch.insights.internal.data.distant.InsightsDistantRepository
import com.algolia.instantsearch.insights.internal.data.local.InsightsLocalRepository
import com.algolia.instantsearch.insights.internal.event.EventResponse
import com.algolia.instantsearch.insights.internal.logging.InsightsLogger
import com.algolia.search.model.insights.InsightsEvent
import kotlinx.coroutines.runBlocking

internal class InsightsEventUploader(
    private val localRepository: InsightsLocalRepository,
    private val distantRepository: InsightsDistantRepository,
) : InsightsUploader {

    override fun uploadAll(): List<EventResponse> {
        val events = localRepository.read()
        InsightsLogger.log("Flushing remaining ${events.size} events.")
        val failedEvents = sendEvents(events).filterEventsWhenException()
        localRepository.overwrite(failedEvents.map { it.event })
        return failedEvents
    }

    private fun sendEvents(events: List<InsightsEvent>): List<EventResponse> {
        return runBlocking {
            events.map { event -> distantRepository.send(event) }
        }
    }

    private fun List<EventResponse>.filterEventsWhenException(): List<EventResponse> {
        return this.filter { it.code == -1 }
    }
}
