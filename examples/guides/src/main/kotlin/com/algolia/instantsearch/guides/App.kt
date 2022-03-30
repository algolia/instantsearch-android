package com.algolia.instantsearch.guides

import android.app.Application
import com.algolia.instantsearch.insights.registerInsights
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.insights.UserToken
import io.ktor.client.features.logging.*

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        registerInsights(
            context = this,
            appId = ApplicationID("latency"),
            apiKey = APIKey("afc3dd66dd1293e2e2736a5a51b05c0a"),
            indexName = IndexName("instant_search"),
            clientLogLevel = LogLevel.ALL
        ).apply {
            loggingEnabled = true
            userToken = UserToken("userToken")
            minBatchSize = 1
        }
    }
}
