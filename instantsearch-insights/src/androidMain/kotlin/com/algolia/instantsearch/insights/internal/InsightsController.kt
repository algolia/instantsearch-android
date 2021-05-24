package com.algolia.instantsearch.insights.internal

import androidx.work.WorkManager
import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.insights.internal.cache.InsightsEventCache
import com.algolia.instantsearch.insights.internal.data.distant.InsightsDistantRepository
import com.algolia.instantsearch.insights.internal.data.local.InsightsLocalRepository
import com.algolia.instantsearch.insights.internal.data.settings.InsightsSettings
import com.algolia.instantsearch.insights.internal.logging.InsightsLogger
import com.algolia.instantsearch.insights.internal.uploader.InsightsEventUploader
import com.algolia.instantsearch.insights.internal.worker.InsightsWorkManager
import com.algolia.instantsearch.insights.shared
import com.algolia.instantsearch.insights.sharedInsights
import com.algolia.search.model.IndexName

internal fun registerInsightsController(
    indexName: IndexName,
    localRepository: InsightsLocalRepository,
    distantRepository: InsightsDistantRepository,
    workManager: WorkManager,
    settings: InsightsSettings,
    configuration: Insights.Configuration = Insights.Configuration(5000, 5000),
): Insights {
    val saver = InsightsEventCache(localRepository)
    val uploader = InsightsEventUploader(localRepository, distantRepository)
    val worker = InsightsWorkManager(workManager, settings)
    return InsightsController(indexName, worker, saver, uploader).also {
        it.userToken = configuration.defaultUserToken
        sharedInsights = it
        InsightsMap[indexName] = it
        InsightsLogger.log("Registering new Insights for indexName $indexName. Previous instance: $it")
    }
}
