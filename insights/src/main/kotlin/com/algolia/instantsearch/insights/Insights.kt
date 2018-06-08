package com.algolia.instantsearch.insights

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import java.util.concurrent.TimeUnit


class Insights internal constructor(
    context: Context,
    private val credentials: Credentials
) {

    private val preferences = context.sharedPreferences(credentials.indexName)

    init {
        PeriodicWorkRequestBuilder<WorkerEvent>(10, TimeUnit.MINUTES).also {
            val inputData = WorkerEvent.buildInputData(credentials)
            val constraints = Constraints().also {
                it.requiredNetworkType = NetworkType.CONNECTED
            }

            it.setInputData(inputData)
            it.setConstraints(constraints)
        }.build()
    }

    fun click(params: EventParameters) {
        process(Event.Click(params))
    }

    fun view(params: EventParameters) {
        process(Event.View(params))
    }

    fun conversion(params: EventParameters) {
        process(Event.Conversion(params))
    }

    private fun process(event: Event) {
        val events = preferences.events
            .map(ConverterStringToEvent::convert)
            .toMutableList()
            .also { it.add(event) }

        preferences.events = ConverterEventToString.convert(events).toSet()
        OneTimeWorkRequestBuilder<WorkerEvent>().also {
            val inputData = WorkerEvent.buildInputData(credentials)
            it.setInputData(inputData)
        }.build()
    }

    companion object {

        private val insightsMap = mutableMapOf<String, Insights>()

        @JvmStatic
        fun register(context: Context, appId: String, apiKey: String, indexName: String): Insights {
            val credentials = Credentials(
                appId = appId,
                apiKey = apiKey,
                indexName = indexName
            )
            val insights = Insights(context, credentials)

            insightsMap[indexName] = insights
            return insights
        }

        @JvmStatic
        fun shared(indexName: String): Insights {
            return insightsMap[indexName] ?: throw InsightsException.CredentialsNotFound()
        }
    }
}
