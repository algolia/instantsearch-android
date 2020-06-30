package com.algolia.instantsearch.insights

import android.content.Context
import android.util.Log
import com.algolia.instantsearch.insights.Insights.Configuration
import com.algolia.instantsearch.insights.internal.database.DatabaseSharedPreferences
import com.algolia.instantsearch.insights.internal.database.InsightsSharedPreferences
import com.algolia.instantsearch.insights.internal.event.EventUploaderAndroidJob
import com.algolia.instantsearch.insights.internal.webservice.Environment
import com.algolia.instantsearch.insights.internal.webservice.WebServiceHttp
import java.util.*

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
    configuration: Configuration
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
 * @param configuration A [Configuration] class.
 * @return An [Insights] instance.
 */
public fun Insights.Companion.register(
    context: Context,
    appId: String,
    apiKey: String,
    indexName: String
): Insights {
    val sharedPreferences = InsightsSharedPreferences(context)
    val eventUploader = EventUploaderAndroidJob(context, sharedPreferences)
    val database = DatabaseSharedPreferences(context, indexName)
    val userToken: String = storedUserToken(sharedPreferences)
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
