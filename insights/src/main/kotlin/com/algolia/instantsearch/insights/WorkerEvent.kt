package com.algolia.instantsearch.insights

import androidx.work.Data
import androidx.work.Worker


internal class WorkerEvent : Worker() {

    private enum class Keys {
        AppId,
        ApiKey,
        Index,
        Environment,
        ReadTimeout,
        ConnectTimeout,
        UploadInterval
    }

    companion object {

        fun buildInputData(credentials: Credentials, configuration: Insights.Configuration): Data {
            return Data.Builder().putAll(
                mapOf(
                    Keys.AppId.name to credentials.appId,
                    Keys.ApiKey.name to credentials.apiKey,
                    Keys.Index.name to credentials.indexName,
                    Keys.Environment.name to configuration.environment.name,
                    Keys.ConnectTimeout.name to configuration.connectTimeout,
                    Keys.ReadTimeout.name to configuration.readTimeout,
                    Keys.UploadInterval.name to configuration.uploadIntervalInMinutes
                )
            ).build()
        }

        fun Data.getInputData(): Pair<Credentials, Insights.Configuration> {
            val credentials = Credentials(
                appId = getString(Keys.AppId.name, null),
                apiKey = getString(Keys.ApiKey.name, null),
                indexName = getString(Keys.Index.name, null)
            )
            val configuration = Insights.Configuration(
                environment = NetworkManager.Environment.valueOf(getString(Keys.Environment.name, null)),
                readTimeout = getInt(Keys.ReadTimeout.name, 5000),
                connectTimeout = getInt(Keys.ConnectTimeout.name, 5000),
                uploadIntervalInMinutes = getLong(Keys.UploadInterval.name, 2L)
            )
            return credentials to configuration
        }
    }

    override fun doWork(): WorkerResult {
        return try {
            val (credentials, configuration) = inputData.getInputData()
            val preferences = applicationContext.sharedPreferences(credentials.indexName)
            val networkManager = NetworkManager(credentials.appId, credentials.apiKey, configuration)
            val failedEvents = preferences.consumeEvents(networkManager.eventConsumer())

            if (failedEvents.isEmpty()) WorkerResult.SUCCESS else WorkerResult.RETRY
        } catch (exception: Exception) {
            WorkerResult.RETRY
        }
    }
}
