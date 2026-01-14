package com.algolia.instantsearch.examples.android

import android.app.Application
import com.algolia.client.model.querysuggestions.LogLevel
import com.algolia.instantsearch.insights.registerInsights
import com.algolia.search.logging.LogLevel
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.insights.UserToken

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        registerInsights(
            context = this,
            appId = "latency",
            apiKey = APIKey("afc3dd66dd1293e2e2736a5a51b05c0a"),
            indexName = IndexName("instant_search"),
            clientLogLevel = LogLevel.ERROR
        ).apply {
            loggingEnabled = true
            userToken = UserToken("userToken")
            minBatchSize = 1
        }
    }
}
