package com.algolia.instantsearch.insights

import android.content.Context
import androidx.work.WorkManager
import com.algolia.instantsearch.insights.exception.InsightsException
import com.algolia.instantsearch.insights.internal.InsightsController
import com.algolia.instantsearch.insights.internal.InsightsMap
import com.algolia.instantsearch.insights.internal.data.distant.InsightsHttpRepository
import com.algolia.instantsearch.insights.internal.data.local.InsightsPrefsRepository
import com.algolia.instantsearch.insights.internal.data.settings.InsightsEventSettings
import com.algolia.instantsearch.insights.internal.extension.clientInsights
import com.algolia.instantsearch.insights.internal.extension.defaultConfiguration
import com.algolia.instantsearch.insights.internal.extension.insightsSettingsPreferences
import com.algolia.instantsearch.insights.internal.extension.insightsSharedPreferences
import com.algolia.instantsearch.insights.internal.sharedInsights
import com.algolia.search.helper.toAPIKey
import com.algolia.search.helper.toApplicationID
import com.algolia.search.helper.toIndexName
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName


/**
 * Access an already registered `Insights` without having to pass the `apiKey` and `appId`.
 *
 * If the index was not register before, it will throw an [InsightsException.IndexNotRegistered] exception.
 * @param indexName The index that is being tracked.
 * @return An Insights instance.
 * @throws InsightsException.IndexNotRegistered if no index was registered as indexName before.
 */
public fun Insights.shared(indexName: IndexName): Insights {
    return InsightsMap[indexName] ?: throw InsightsException.IndexNotRegistered()
}

/**
 * Access the latest registered `Insights` instance, if any.
 */
public var Insights.shared: Insights?
    get() = if (sharedInsights != null) sharedInsights else throw InsightsException.IndexNotRegistered()
    set(value) {
        sharedInsights = value
    }

/**
 * Register your index with a given appId and apiKey.
 *
 * @param context An Android Context.
 * @param appId The given app id for which you want to track the events.
 * @param apiKey The API Key for your `appId`.
 * @param indexName The index that is being tracked.
 * @param configuration A Configuration class.
 */
public fun Insights.register(
    context: Context,
    appId: String,
    apiKey: String,
    indexName: String,
    configuration: Insights.Configuration? = null,
): Insights {
    return register(context, appId.toApplicationID(), apiKey.toAPIKey(), indexName.toIndexName(), configuration)
}

/**
 * Register your index with a given appId and apiKey.
 *
 * @param context An Android Context.
 * @param appId The given app id for which you want to track the events.
 * @param apiKey The API Key for your `appId`.
 * @param indexName The index that is being tracked.
 * @param configuration insights configuration
 */
public fun Insights.register(
    context: Context,
    appId: ApplicationID,
    apiKey: APIKey,
    indexName: IndexName,
    configuration: Insights.Configuration? = null,
): Insights {
    val localRepository = InsightsPrefsRepository(context.insightsSharedPreferences(indexName))
    val settings = InsightsEventSettings(context.insightsSettingsPreferences())
    val config = configuration ?: defaultConfiguration(settings)
    val distantRepository = InsightsHttpRepository(clientInsights(appId, apiKey, config))
    val workManager = WorkManager.getInstance(context)
    return InsightsController.register(
        indexName = indexName,
        localRepository = localRepository,
        distantRepository = distantRepository,
        workManager = workManager,
        settings = settings,
        configuration = config
    )
}
