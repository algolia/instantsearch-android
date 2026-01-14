package com.algolia.instantsearch.insights

import android.content.Context
import androidx.work.WorkManager
import com.algolia.client.model.querysuggestions.LogLevel
import com.algolia.instantsearch.insights.exception.InsightsException
import com.algolia.instantsearch.insights.internal.InsightsMap
import com.algolia.instantsearch.insights.internal.data.distant.InsightsHttpRepository
import com.algolia.instantsearch.insights.internal.data.local.InsightsPrefsRepository
import com.algolia.instantsearch.insights.internal.data.settings.InsightsEventSettings
import com.algolia.instantsearch.insights.internal.extension.clientInsights
import com.algolia.instantsearch.insights.internal.extension.defaultConfiguration
import com.algolia.instantsearch.insights.internal.extension.insightsSettingsPreferences
import com.algolia.instantsearch.insights.internal.extension.insightsSharedPreferences
import com.algolia.instantsearch.insights.internal.registerInsightsController
import com.algolia.instantsearch.migration2to3.IndexName


/**
 * Access the latest registered `Insights` instance, if any, otherwise throws  [InsightsException.IndexNotRegistered].
 * Thread safety is not guaranteed.
 */
public var sharedInsights: Insights? = null
    get() = field ?: throw InsightsException.IndexNotRegistered()

/**
 * Access an already registered `Insights` without having to pass the `apiKey` and `appId`.
 *
 * If the index was not register before, it will throw an [InsightsException.IndexNotRegistered] exception.
 * @param indexName The index that is being tracked.
 * @return An Insights instance.
 * @throws InsightsException.IndexNotRegistered if no index was registered as indexName before.
 */
public fun sharedInsights(indexName: IndexName): Insights {
    return InsightsMap[indexName] ?: throw InsightsException.IndexNotRegistered()
}

/**
 * Register your index with a given appId and apiKey.
 *
 * @param context An Android Context.
 * @param appId The given app id for which you want to track the events.
 * @param apiKey The API Key for your `appId`.
 * @param indexName The index that is being tracked.
 * @param configuration insights configuration
 * @param clientLogLevel insights API client log level
 */
public fun registerInsights(
    context: Context,
    appId: String,
    apiKey: String,
    indexName: String,
    configuration: Insights.Configuration? = null,
    clientLogLevel: LogLevel = LogLevel.SKIP
): Insights {
    val localRepository = InsightsPrefsRepository(context.insightsSharedPreferences(indexName))
    val settings = InsightsEventSettings(context.insightsSettingsPreferences())
    val config = configuration ?: defaultConfiguration(settings)
    val distantRepository = InsightsHttpRepository(clientInsights(appId, apiKey, config, clientLogLevel))
    val workManager = WorkManager.getInstance(context)
    return registerInsightsController(
        indexName = indexName,
        localRepository = localRepository,
        distantRepository = distantRepository,
        workManager = workManager,
        settings = settings,
        configuration = config
    )
}
