package com.algolia.instantsearch.insights.internal

import android.util.Log
import androidx.work.WorkManager
import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.insights.exception.InsightsException
import com.algolia.instantsearch.insights.internal.cache.InsightsCache
import com.algolia.instantsearch.insights.internal.cache.InsightsEventCache
import com.algolia.instantsearch.insights.internal.data.distant.InsightsDistantRepository
import com.algolia.instantsearch.insights.internal.data.local.InsightsLocalRepository
import com.algolia.instantsearch.insights.internal.data.settings.InsightsSettings
import com.algolia.instantsearch.insights.internal.logging.InsightsLogger
import com.algolia.instantsearch.insights.internal.uploader.InsightsEventUploader
import com.algolia.instantsearch.insights.internal.uploader.InsightsUploader
import com.algolia.instantsearch.insights.internal.worker.InsightsManager
import com.algolia.instantsearch.insights.internal.worker.InsightsWorkManager
import com.algolia.search.model.IndexName
import com.algolia.search.model.ObjectID
import com.algolia.search.model.QueryID
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.insights.EventName
import com.algolia.search.model.insights.InsightsEvent
import com.algolia.search.model.insights.UserToken

/**
 * Main class used for interacting with the InstantSearch Insights library.
 * In order to send insights, you first need to register an APP ID and API key for a given Index.
 * Once registered, you can simply call `Insights.shared(index: String)` to send your events.
 */
internal class InsightsController(
    private val indexName: IndexName,
    private val worker: InsightsManager,
    private val cache: InsightsCache,
    internal val uploader: InsightsUploader,
) : Insights {

    override var enabled: Boolean = true
    override var userToken: UserToken? = null
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

    private fun userTokenOrThrow(): UserToken = userToken ?: throw InsightsException.NoUserToken()

    init {
        worker.startPeriodicUpload()
    }

    // region Event tracking methods
    override fun viewedObjectIDs(
        eventName: EventName,
        objectIDs: List<ObjectID>,
        timestamp: Long,
    ): Unit = viewed(
        InsightsEvent.View(
            indexName = indexName,
            eventName = eventName,
            userToken = userTokenOrThrow(),
            timestamp = timestamp,
            resources = InsightsEvent.Resources.ObjectIDs(objectIDs)
        )
    )

    override fun viewedFilters(
        eventName: EventName,
        filters: List<Filter.Facet>,
        timestamp: Long,
    ): Unit = viewed(
        InsightsEvent.View(
            indexName = indexName,
            eventName = eventName,
            userToken = userTokenOrThrow(),
            timestamp = timestamp,
            resources = InsightsEvent.Resources.Filters(filters)
        )
    )

    override fun clickedObjectIDs(
        eventName: EventName,
        objectIDs: List<ObjectID>,
        timestamp: Long,
    ): Unit = clicked(
        InsightsEvent.Click(
            indexName = indexName,
            eventName = eventName,
            userToken = userTokenOrThrow(),
            timestamp = timestamp,
            resources = InsightsEvent.Resources.ObjectIDs(objectIDs)
        )
    )

    override fun clickedFilters(
        eventName: EventName,
        filters: List<Filter.Facet>,
        timestamp: Long,
    ): Unit = clicked(
        InsightsEvent.Click(
            indexName = indexName,
            eventName = eventName,
            userToken = userTokenOrThrow(),
            timestamp = timestamp,
            resources = InsightsEvent.Resources.Filters(filters)
        )
    )

    override fun clickedObjectIDsAfterSearch(
        eventName: EventName,
        queryID: QueryID,
        objectIDs: List<ObjectID>,
        positions: List<Int>,
        timestamp: Long,
    ): Unit = clicked(
        InsightsEvent.Click(
            indexName = indexName,
            eventName = eventName,
            userToken = userTokenOrThrow(),
            timestamp = timestamp,
            resources = InsightsEvent.Resources.ObjectIDs(objectIDs),
            queryID = queryID,
            positions = positions
        )
    )

    override fun convertedFilters(
        eventName: EventName,
        filters: List<Filter.Facet>,
        timestamp: Long,
    ): Unit = converted(
        InsightsEvent.Conversion(
            indexName = indexName,
            eventName = eventName,
            userToken = userTokenOrThrow(),
            timestamp = timestamp,
            resources = InsightsEvent.Resources.Filters(filters)
        )
    )

    override fun convertedObjectIDs(
        eventName: EventName,
        objectIDs: List<ObjectID>,
        timestamp: Long,
    ): Unit = converted(
        InsightsEvent.Conversion(
            indexName = indexName,
            eventName = eventName,
            userToken = userTokenOrThrow(),
            timestamp = timestamp,
            resources = InsightsEvent.Resources.ObjectIDs(objectIDs)
        )
    )

    override fun convertedObjectIDsAfterSearch(
        eventName: EventName,
        queryID: QueryID,
        objectIDs: List<ObjectID>,
        timestamp: Long,
    ): Unit = converted(
        InsightsEvent.Conversion(
            indexName = indexName,
            eventName = eventName,
            userToken = userTokenOrThrow(),
            timestamp = timestamp,
            resources = InsightsEvent.Resources.ObjectIDs(objectIDs),
            queryID = queryID
        )
    )

    override fun viewed(event: InsightsEvent.View): Unit = track(event)

    override fun clicked(event: InsightsEvent.Click): Unit = track(event)

    override fun converted(event: InsightsEvent.Conversion): Unit = track(event)

    override fun track(event: InsightsEvent) {
        if (enabled) {
            cache.save(event)
            if (cache.size() >= minBatchSize) {
                worker.startOneTimeUpload()
            }
        }
    }

    // endregion

    companion object {

        /**
         * Register your index with a given appId and apiKey.
         * @param indexName The index that is being tracked.
         * @param localRepository local storage
         * @param distantRepository server storage
         * @param workManager jobs scheduler
         * @param settings settings storage
         * @param configuration A Configuration class.
         */
        internal fun register(
            indexName: IndexName,
            localRepository: InsightsLocalRepository,
            distantRepository: InsightsDistantRepository,
            workManager: WorkManager,
            settings: InsightsSettings,
            configuration: Insights.Configuration = Insights.Configuration(5000, 5000),
        ): Insights {
            val saver = InsightsEventCache(localRepository)
            val uploader = InsightsEventUploader(localRepository, distantRepository)
            val worker = InsightsWorkManager(workManager, settings)
            return InsightsController(indexName, worker, saver, uploader).also {
                it.userToken = configuration.defaultUserToken
                Insights.shared = it
                InsightsMap[indexName] = it
                Log.d("Insights", "Registering new Insights for indexName $indexName. Previous instance: $it")
            }
        }
    }
}
