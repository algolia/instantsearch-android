package com.algolia.instantsearch.insights

import android.content.Context
import com.algolia.instantsearch.insights.Insights.Configuration
import com.algolia.instantsearch.insights.internal.database.DatabaseSharedPreferences
import com.algolia.instantsearch.insights.internal.event.EventUploaderAndroidJob
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
fun Insights.register(
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
        environment = WebServiceHttp.Environment.Prod,
        connectTimeoutInMilliseconds = configuration.connectTimeoutInMilliseconds,
        readTimeoutInMilliseconds = configuration.readTimeoutInMilliseconds
    )

    return Insights.register(eventUploader, database, webService, indexName, configuration)
}
