package com.algolia.instantsearch.examples.android

import android.app.Application
import com.algolia.client.model.querysuggestions.LogLevel
import com.algolia.instantsearch.insights.registerInsights

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        registerInsights(
            context = this,
            appId = "latency",
            apiKey = "afc3dd66dd1293e2e2736a5a51b05c0a",
            indexName = "instant_search",
            clientLogLevel = LogLevel.ERROR
        ).apply {
            loggingEnabled = true
            userToken = "userToken"
            minBatchSize = 1
        }
    }
}
