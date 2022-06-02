package com.algolia.instantsearch.benchmark

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.algolia.instantsearch.benchmark.utils.AndroidBenchmarkRule
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SearcherBenchmark {

    @get:Rule
    val rule = AndroidBenchmarkRule()

    @Test
    fun searcher() {
        val searcher = HitsSearcher(
            applicationID = ApplicationID("latency"),
            apiKey = APIKey("afc3dd66dd1293e2e2736a5a51b05c0a"),
            indexName = IndexName("instant_search")
        )

        rule.activityTestRule.runOnUiThread {

        }
    }
}
