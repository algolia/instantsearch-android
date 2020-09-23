package com.algolia.instantsearch.insights

import android.content.Context
import android.util.Log
import com.algolia.instantsearch.insights.event.EventUploader
import com.algolia.instantsearch.insights.internal.converter.ConverterEventToEventInternal
import com.algolia.instantsearch.insights.internal.database.Database
import com.algolia.instantsearch.insights.internal.database.DatabaseSharedPreferences
import com.algolia.instantsearch.insights.internal.database.InsightsSharedPreferences
import com.algolia.instantsearch.insights.internal.event.EventInternal
import com.algolia.instantsearch.insights.internal.event.EventUploaderAndroidJob
import com.algolia.instantsearch.insights.internal.logging.InsightsLogger
import com.algolia.instantsearch.insights.internal.webservice.Environment
import com.algolia.instantsearch.insights.internal.webservice.WebService
import com.algolia.instantsearch.insights.internal.webservice.WebServiceHttp
import com.algolia.search.model.IndexName
import com.algolia.search.model.ObjectID
import com.algolia.search.model.QueryID
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.insights.EventName
import com.algolia.search.model.insights.InsightsEvent
import com.algolia.search.model.insights.UserToken
import java.util.UUID

/**
 * Main class used for interacting with the InstantSearch Insights library.
 * In order to send insights, you first need to register an APP ID and API key for a given Index.
 * Once registered, you can simply call `Insights.shared(index: String)` to send your events.
 */
public class Insights internal constructor(
    private val indexName: IndexName,
    private val eventUploader: EventUploader,
    internal val database: Database,
    internal val webService: WebService,
) : HitsAfterSearchTrackable, FilterTrackable {

    /**
     * Change this variable to `true` or `false` to enable or disable logging.
     * Use a filter on tag `Algolia Insights` to see all logs generated by the Insights library.
     */
    @Suppress("unused") // setter does side-effect
    public var loggingEnabled: Boolean = false
        set(value) {
            field = value
            InsightsLogger.enabled[indexName] = value
        }

    /**
     * Change this variable to `true` or `false` to disable Insights, opting-out the current session from tracking.
     */
    public var enabled: Boolean = true

    /**
     * Change this variable to change the default debouncing interval. Values lower than 15 minutes will be ignored.
     */
    public var debouncingIntervalInMinutes: Long? = null
        set(value) {
            value?.let { eventUploader.setInterval(value) }
        }

    /**
     * Set a user identifier that will override any event's.
     *
     * Depending if the user is logged-in or not, several strategies can be used from a sessionId to a technical identifier.
     * You should always send pseudonymous or anonymous userTokens.
     */
    public var userToken: UserToken? = null

    private fun userTokenOrThrow(): UserToken = userToken ?: throw InsightsException.NoUserToken()

    /**
     * Change this variable to change the default amount of event sent at once.
     */
    public var minBatchSize: Int = 10

    init {
        eventUploader.startPeriodicUpload()
    }

    // region Event tracking methods
    public override fun viewedObjectIDs(
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

    public override fun viewedFilters(
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

    public override fun clickedObjectIDs(
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

    public override fun clickedFilters(
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

    public override fun clickedObjectIDsAfterSearch(
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

    public override fun convertedFilters(
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

    public override fun convertedObjectIDs(
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

    public override fun convertedObjectIDsAfterSearch(
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

    /**
     * Tracks a View event constructed manually.
     */
    public fun viewed(event: InsightsEvent.View): Unit = track(event)

    /**
     * Tracks a Click event constructed manually.
     */
    public fun clicked(event: InsightsEvent.Click): Unit = track(event)

    /**
     * Tracks a Conversion event, constructed manually.
     */
    public fun converted(event: InsightsEvent.Conversion): Unit = track(event)

    /**
     * Method for tracking an event.
     * For a complete description of events see our [documentation][https://www.algolia.com/doc/rest-api/insights/?language=android#push-events].
     * @param [event] An event that you want to track.
     */
    public fun track(event: InsightsEvent) {
        val insightsEvent = ConverterEventToEventInternal.convert(event to event.indexName)
        track(insightsEvent)
    }

    private fun track(event: EventInternal) {
        if (enabled) {
            database.append(event)
            if (database.count() >= minBatchSize) {
                eventUploader.startOneTimeUpload()
            }
        }
    }

    // endregion

    override fun toString(): String {
        return "Insights(indexName='$indexName', webService=$webService)"
    }

    public companion object {

        internal val insightsMap = mutableMapOf<IndexName, Insights>()

        /**
         * Register your index with a given appId and apiKey.
         * @param eventUploader event uploader
         * @param database local storage
         * @param webService web service
         * @param indexName The index that is being tracked.
         * @param configuration A [Configuration] class.
         * @return An [Insights] instance.
         */
        internal fun register(
            eventUploader: EventUploader,
            database: Database,
            webService: WebService,
            indexName: IndexName,
            configuration: Configuration = Configuration(5000, 5000),
        ): Insights {

            val insights = Insights(indexName, eventUploader, database, webService)
            insights.userToken = configuration.defaultUserToken

            val previousInsights = insightsMap.put(indexName, insights)
            previousInsights?.let {
                InsightsLogger.log("Registering new Insights for indexName $indexName. Previous instance: $insights")
            }
            shared = insights
            return insights
        }

        /**
         * Access an already registered `Insights` without having to pass the `apiKey` and `appId`.
         *
         * If the index was not register before, it will throw an [InsightsException.IndexNotRegistered] exception.
         * @param indexName The index that is being tracked.
         * @return An [Insights] instance.
         * @throws InsightsException.IndexNotRegistered if no index was registered as [indexName] before.
         */
        @JvmStatic
        public fun shared(indexName: IndexName): Insights {
            return insightsMap[indexName]
                ?: throw InsightsException.IndexNotRegistered()
        }

        /**
         * Access the latest registered `Insights` instance, if any.
         */
        @JvmStatic
        public var shared: Insights? = null
            @JvmName("shared")
            get() = if (field != null) field else throw InsightsException.IndexNotRegistered()

        /**
         * Register your index with a given appId and apiKey.
         * @param context A [Context].
         * @param appId The given app id for which you want to track the events.
         * @param apiKey The API Key for your `appId`.
         * @param indexName The index that is being tracked.
         * @param configuration A [Configuration] class.
         * @return An [Insights] instance.
         */
        @JvmStatic
        public fun register(
            context: Context,
            appId: String,
            apiKey: String,
            indexName: IndexName,
            configuration: Configuration,
        ): Insights {
            val preferences = InsightsSharedPreferences(context)
            val eventUploader = EventUploaderAndroidJob(context, preferences)
            val database = DatabaseSharedPreferences(context, indexName)
            val webService = WebServiceHttp(
                appId = appId,
                apiKey = apiKey,
                environment = Environment.Prod,
                connectTimeoutInMilliseconds = configuration.connectTimeoutInMilliseconds,
                readTimeoutInMilliseconds = configuration.readTimeoutInMilliseconds
            )

            return register(eventUploader, database, webService, indexName, configuration)
        }

        /**
         * Register your index with a given appId and apiKey.
         * @param context A [Context].
         * @param appId The given app id for which you want to track the events.
         * @param apiKey The API Key for your `appId`.
         * @param indexName The index that is being tracked.
         * @return An [Insights] instance.
         */
        @JvmStatic
        public fun register(
            context: Context,
            appId: String,
            apiKey: String,
            indexName: IndexName,
        ): Insights {
            val sharedPreferences = InsightsSharedPreferences(context)
            val eventUploader = EventUploaderAndroidJob(context, sharedPreferences)
            val database = DatabaseSharedPreferences(context, indexName)
            val userToken = UserToken(storedUserToken(sharedPreferences))
            Log.d("Insights", "Insights user token: $userToken")
            val configuration = Configuration(5000, 5000, userToken)

            val webService = WebServiceHttp(
                appId = appId,
                apiKey = apiKey,
                environment = Environment.Prod,
                connectTimeoutInMilliseconds = configuration.connectTimeoutInMilliseconds,
                readTimeoutInMilliseconds = configuration.readTimeoutInMilliseconds
            )

            return register(eventUploader, database, webService, indexName, configuration)
        }

        private fun storedUserToken(preferences: InsightsSharedPreferences): String {
            val userToken = preferences.userToken
            if (userToken != null) return userToken

            return UUID.randomUUID().toString().also {
                preferences.userToken = it
            }
        }
    }

    /**
     * Insights configuration.
     * @param connectTimeoutInMilliseconds Maximum amount of time in milliseconds before a connect timeout.
     * @param readTimeoutInMilliseconds Maximum amount of time in milliseconds before a read timeout.
     */
    public class Configuration @JvmOverloads constructor(
        public val connectTimeoutInMilliseconds: Int,
        public val readTimeoutInMilliseconds: Int,
        public val defaultUserToken: UserToken? = null,
    )
}
