package com.algolia.instantsearch.insights

import android.content.Context
import android.util.Log
import androidx.work.WorkManager
import com.algolia.instantsearch.insights.internal.data.distant.InsightsDistantRepository
import com.algolia.instantsearch.insights.internal.data.distant.InsightsHttpRepository
import com.algolia.instantsearch.insights.internal.data.local.InsightsLocalRepository
import com.algolia.instantsearch.insights.internal.data.local.InsightsPrefsRepository
import com.algolia.instantsearch.insights.internal.data.settings.InsightsEventSettings
import com.algolia.instantsearch.insights.internal.data.settings.InsightsSettings
import com.algolia.instantsearch.insights.internal.extension.insightsSharedPreferences
import com.algolia.instantsearch.insights.internal.logging.InsightsLogger
import com.algolia.instantsearch.insights.internal.saver.InsightsEventSaver
import com.algolia.instantsearch.insights.internal.saver.InsightsSaver
import com.algolia.instantsearch.insights.internal.uploader.InsightsEventUploader
import com.algolia.instantsearch.insights.internal.uploader.InsightsUploader
import com.algolia.instantsearch.insights.internal.worker.InsightsManager
import com.algolia.instantsearch.insights.internal.worker.InsightsWorkManager
import com.algolia.search.client.ClientInsights
import com.algolia.search.configuration.ConfigurationInsights
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
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
    private val worker: InsightsManager,
    private val saver: InsightsSaver,
    internal val uploader: InsightsUploader,
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
            value?.let { worker.setInterval(value) }
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
        worker.startPeriodicUpload()
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
        if (enabled) {
            saver.save(event)
            if (saver.size() >= minBatchSize) {
                worker.startOneTimeUpload()
            }
        }
    }

    // endregion

    public companion object {

        private const val INSIGHTS_SHARED_PREFS = "InsightsEvents"
        internal val insightsMap = mutableMapOf<IndexName, Insights>()

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
            val localRepository = InsightsPrefsRepository(context.insightsSharedPreferences(indexName))
            val distantRepository = InsightsHttpRepository(clientInsights(appId, apiKey, configuration))
            val workManager = WorkManager.getInstance(context)
            val settings = InsightsEventSettings(insightsSettingsPrefs(context))
            return register(indexName, localRepository, distantRepository, workManager, settings, configuration)
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
            val localRepository = InsightsPrefsRepository(context.insightsSharedPreferences(indexName))
            val settings = InsightsEventSettings(insightsSettingsPrefs(context))
            val userToken = UserToken(storedUserToken(settings))
            Log.d("Insights", "Insights user token: $userToken")
            val configuration = Configuration(5000, 5000, userToken)
            val distantRepository = InsightsHttpRepository(clientInsights(appId, apiKey, configuration))
            val workManager = WorkManager.getInstance(context)
            return register(indexName, localRepository, distantRepository, workManager, settings, configuration)
        }

        /**
         * Register your index with a given appId and apiKey.
         * @param indexName The index that is being tracked.
         * @param localRepository local storage
         * @param distantRepository server storage
         * @param jobManager jobs scheduler
         * @param settings settings storage
         * @param configuration A [Configuration] class.
         * @return An [Insights] instance.
         */
        internal fun register(
            indexName: IndexName,
            localRepository: InsightsLocalRepository,
            distantRepository: InsightsDistantRepository,
            workManager: WorkManager,
            settings: InsightsSettings,
            configuration: Configuration = Configuration(5000, 5000),
        ): Insights {
            val saver = InsightsEventSaver(localRepository)
            val uploader = InsightsEventUploader(localRepository, distantRepository)
            val worker = InsightsWorkManager(workManager, settings)
            val insights = Insights(indexName, worker, saver, uploader)
            insights.userToken = configuration.defaultUserToken
            insightsMap.put(indexName, insights)?.let {
                InsightsLogger.log("Registering new Insights for indexName $indexName. Previous instance: $it")
            }
            shared = insights
            return insights
        }

        private fun insightsSettingsPrefs(context: Context) =
            context.getSharedPreferences(INSIGHTS_SHARED_PREFS, Context.MODE_PRIVATE)

        private fun storedUserToken(settings: InsightsSettings): String {
            val userToken = settings.userToken
            if (userToken != null) return userToken

            return UUID.randomUUID().toString().also {
                settings.userToken = it
            }
        }

        private fun clientInsights(appId: String, apiKey: String, configuration: Configuration): ClientInsights {
            return ClientInsights(
                ConfigurationInsights(
                    applicationID = ApplicationID(appId),
                    apiKey = APIKey(apiKey),
                    writeTimeout = configuration.connectTimeoutInMilliseconds,
                    readTimeout = configuration.readTimeoutInMilliseconds
                )
            )
        }
    }

    /**
     * Insights configuration.
     * @param connectTimeoutInMilliseconds Maximum amount of time in milliseconds before a connect timeout.
     * @param readTimeoutInMilliseconds Maximum amount of time in milliseconds before a read timeout.
     */
    public class Configuration @JvmOverloads constructor(
        public val connectTimeoutInMilliseconds: Long,
        public val readTimeoutInMilliseconds: Long,
        public val defaultUserToken: UserToken? = null,
    )
}
