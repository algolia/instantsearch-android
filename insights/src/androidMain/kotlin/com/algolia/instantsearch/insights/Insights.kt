package com.algolia.instantsearch.insights

import android.content.Context
import com.algolia.instantsearch.insights.Insights.Configuration
import com.algolia.instantsearch.insights.internal.database.DatabaseSharedPreferences
import com.algolia.instantsearch.insights.internal.event.EventUploaderAndroidJob
import com.algolia.instantsearch.insights.internal.webservice.Environment
import com.algolia.instantsearch.insights.internal.webservice.WebServiceHttp

/**
 * Register your index with a given appId and apiKey.
 * @param context A [Context].
 * @param appId The given app id for which you want to track the events.
 * @param apiKey The API Key for your `appId`.
 * @param indexName The index that is being tracked.
 * @param configuration A [Configuration] class.
 * @return An [Insights] instance.
 */
public fun Insights.Companion.register(
    context: Context,
    appId: String,
    apiKey: String,
    indexName: String,
    configuration: Configuration = Configuration(5000, 5000)
): Insights {
    val eventUploader = EventUploaderAndroidJob(context)
    val database = DatabaseSharedPreferences(context, indexName)
    val webService = WebServiceHttp(
        appId = appId,
        apiKey = apiKey,
        environment = Environment.Prod,
        connectTimeoutInMilliseconds = configuration.connectTimeoutInMilliseconds,
        readTimeoutInMilliseconds = configuration.readTimeoutInMilliseconds
    )

    return Insights.register(eventUploader, database, webService, indexName, configuration)
}

/**
 * Register your index with a given appId and apiKey.
 * This function is meant to improve Java DX.
 *
 * @param context A [Context].
 * @param appId The given app id for which you want to track the events.
 * @param apiKey The API Key for your `appId`.
 * @param indexName The index that is being tracked.
 * @param configuration A [Configuration] class.
 * @return An [Insights] instance.
 */
@JvmOverloads
public fun register(
    context: Context,
    appId: String,
    apiKey: String,
    indexName: String,
    configuration: Configuration = Configuration(5000, 5000)
): Insights = Insights.register(context, appId, apiKey, indexName, configuration)
