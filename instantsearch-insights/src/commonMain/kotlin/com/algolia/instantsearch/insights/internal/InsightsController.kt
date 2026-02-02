
package com.algolia.instantsearch.insights.internal

import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.insights.internal.cache.InsightsCache
import com.algolia.instantsearch.insights.internal.data.local.mapper.FilterFacetMapper
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.EventType
import com.algolia.instantsearch.insights.internal.extension.currentTimeMillis
import com.algolia.instantsearch.insights.internal.extension.randomUUID
import com.algolia.instantsearch.insights.internal.logging.InsightsLogger
import com.algolia.instantsearch.insights.internal.uploader.InsightsUploader
import com.algolia.instantsearch.insights.internal.worker.InsightsManager
import com.algolia.instantsearch.filter.Filter

/**
 * Main class used for interacting with the InstantSearch Insights library.
 * In order to send insights, you first need to register an APP ID and API key for a given Index.
 * Once registered, you can simply call `Insights.shared(index: String)` to send your events.
 */
internal class InsightsController(
    private val indexName: String,
    private val worker: InsightsManager,
    private val cache: InsightsCache,
    internal val uploader: InsightsUploader,
    private val generateTimestamps: Boolean
) : Insights {

    override val applicationID: String get() = uploader.applicationID
    override val apiKey: String get() = uploader.apiKey

    override var enabled: Boolean = true
    override var userToken: String? = null
    override var minBatchSize: Int = 10
    override var debouncingIntervalInMinutes: Long? = null
        set(value) {
            value?.let { worker.setInterval(value) }
        }
    override var loggingEnabled: Boolean = false
        set(value) {
            field = value
            InsightsLogger.enabled[indexName] = value
        }

    private fun userTokenOrGenerate(): String {
        return userToken ?: randomUUID().also { userToken = it }
    }

    init {
        worker.startPeriodicUpload()
    }

    // region Event tracking methods
    override fun viewedObjectIDs(
        eventName: String,
        objectIDs: List<String>,
        timestamp: Long?,
    ) = track(buildEventDO(EventType.View, eventName, timestamp, objectIDs = objectIDs))

    override fun viewedFilters(
        eventName: String,
        filters: List<Filter.Facet>,
        timestamp: Long?,
    ) = track(buildEventDO(EventType.View, eventName, timestamp, filters = filters))

    override fun clickedObjectIDs(
        eventName: String,
        objectIDs: List<String>,
        timestamp: Long?,
    ) = track(buildEventDO(EventType.Click, eventName, timestamp, objectIDs = objectIDs))

    override fun clickedFilters(
        eventName: String,
        filters: List<Filter.Facet>,
        timestamp: Long?,
    ) = track(buildEventDO(EventType.Click, eventName, timestamp, filters = filters))

    override fun clickedObjectIDsAfterSearch(
        eventName: String,
        queryID: String,
        objectIDs: List<String>,
        positions: List<Int>,
        timestamp: Long?,
    ) = track(buildEventDO(EventType.Click, eventName, timestamp, queryID, objectIDs, positions))

    override fun convertedFilters(
        eventName: String,
        filters: List<Filter.Facet>,
        timestamp: Long?,
    ) = track(buildEventDO(EventType.Conversion, eventName, timestamp, filters = filters))

    override fun convertedObjectIDs(
        eventName: String,
        objectIDs: List<String>,
        timestamp: Long?,
    ) = track(buildEventDO(EventType.Conversion, eventName, timestamp, objectIDs = objectIDs))

    override fun convertedObjectIDsAfterSearch(
        eventName: String,
        queryID: String,
        objectIDs: List<String>,
        timestamp: Long?,
    ) = track(buildEventDO(EventType.Conversion, eventName, timestamp, queryID, objectIDs))

    fun track(event: InsightsEventDO) {
        val insightEvent = effectiveEvent(event)
        if (enabled) {
            cache.save(insightEvent)
            if (cache.size() >= minBatchSize) {
                worker.startOneTimeUpload()
            }
        }
    }

    // endregion

    private fun buildEventDO(
        eventType: EventType,
        eventName: String,
        timestamp: Long?,
        queryID: String? = null,
        objectIDs: List<String>? = null,
        positions: List<Int>? = null,
        filters: List<Filter.Facet>? = null,
    ): InsightsEventDO {
        val builder = InsightsEventDO.Builder().apply {
            this.eventType = eventType
            this.eventName = eventName
            this.indexName = indexName
            this.userToken = userTokenOrGenerate()
            this.timestamp = timestamp
            this.queryID = queryID
            this.objectIDs = objectIDs
            this.positions = positions
            this.filters = filters?.map { FilterFacetMapper.map(it) }
        }
        return builder.build()
    }

    /**
     * Get effective insight event.
     * Timestamp defaults to [currentTimeMillis] if [event]'s timestamp is `null` and timestamp generation is enabled.
     */
    private fun effectiveEvent(event: InsightsEventDO): InsightsEventDO {
        return if (generateTimestamps && event.timestamp == null) event.copy(timestamp = currentTimeMillis) else event
    }
}
