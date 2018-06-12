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

        fun buildInputData(credentials: Credentials, configuration: Insights.Configuration, environment: NetworkManager.Environment): Data {
            return Data.Builder().putAll(
                mapOf(
                    Keys.AppId.name to credentials.appId,
                    Keys.ApiKey.name to credentials.apiKey,
                    Keys.Index.name to credentials.indexName,
                    Keys.Environment.name to environment.name,
                    Keys.ConnectTimeout.name to configuration.connectTimeoutInMilliseconds,
                    Keys.ReadTimeout.name to configuration.readTimeoutInMilliseconds,
                    Keys.UploadInterval.name to configuration.uploadIntervalInSeconds
                )
            ).build()
        }

        fun Data.getInputData(): Triple<Credentials, Insights.Configuration, NetworkManager.Environment> {
            val credentials = Credentials(
                appId = getString(Keys.AppId.name, null),
                apiKey = getString(Keys.ApiKey.name, null),
                indexName = getString(Keys.Index.name, null)
            )
            val configuration = Insights.Configuration(
                readTimeoutInMilliseconds = getInt(Keys.ReadTimeout.name, 5000),
                connectTimeoutInMilliseconds = getInt(Keys.ConnectTimeout.name, 5000),
                uploadIntervalInSeconds = getLong(Keys.UploadInterval.name, 30L)
            )
            val environment = NetworkManager.Environment.valueOf(getString(Keys.Environment.name, null))
            return Triple(credentials, configuration, environment)
        }
    }

    override fun doWork(): WorkerResult {
        val (credentials, configuration, environment) = inputData.getInputData()
        return try {
            val preferences = applicationContext.sharedPreferences(credentials.indexName)
            val networkManager = NetworkManager(credentials.appId, credentials.apiKey, environment, configuration)
            val failedEvents = preferences.consumeEvents(networkManager.eventConsumer(credentials.indexName))

            Logger.log(credentials.indexName, "Flushing remaining events.")
            if (failedEvents.isEmpty()) WorkerResult.SUCCESS else WorkerResult.FAILURE
        } catch (exception: Exception) {
            Logger.log(credentials.indexName, "Error syncing event: ${exception.localizedMessage}.")
            WorkerResult.FAILURE
        }
    }
}
