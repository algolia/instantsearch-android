package com.algolia.instantsearch.insights

import androidx.work.Data
import androidx.work.Worker


internal class WorkerEvent : Worker() {

    private enum class Keys {
        AppId,
        ApiKey,
        Index
    }

    companion object {

        fun buildInputData(credentials: Credentials): Data {
            return Data.Builder().putAll(
                mapOf(
                    Keys.AppId.name to credentials.appId,
                    Keys.ApiKey.name to credentials.apiKey,
                    Keys.Index.name to credentials.indexName
                )
            ).build()
        }

        fun Data.getInputData(): Triple<String, String, String> {
            return Triple(
                getString(Keys.AppId.name, null),
                getString(Keys.ApiKey.name, null),
                getString(Keys.Index.name, null)
            )
        }
    }

    override fun doWork(): WorkerResult {
        return try {
            val (appId, apiKey, indexName) = inputData.getInputData()
            val preferences = applicationContext.sharedPreferences(indexName)
            val networkManager = NetworkManager(appId, apiKey)
            val failedEvents = preferences.consumeEvents(networkManager.eventConsumer())

            if (failedEvents.isEmpty()) WorkerResult.SUCCESS else WorkerResult.RETRY
        } catch (exception: Exception) {
            WorkerResult.RETRY
        }
    }
}
