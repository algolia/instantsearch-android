package stats

import com.algolia.client.model.search.SearchResponse
import com.algolia.instantsearch.stats.DefaultStatsPresenter
import shouldEqual
import kotlin.test.Test

class TestStatsPresenter {

    @Test
    fun responseNull() {
        val presenter = DefaultStatsPresenter()

        presenter(null) shouldEqual ""
    }

    @Test
    fun responseEmpty() {
        val presenter = DefaultStatsPresenter()

        presenter(SearchResponse(hits = emptyList(), query = "", params = "")) shouldEqual ""
    }

    @Test
    fun responseNbHits() {
        val presenter = DefaultStatsPresenter()

        presenter(SearchResponse(hits = emptyList(), query = "", params = "", nbHits = 10)) shouldEqual "10 hits"
    }

    @Test
    fun responseNbHitsAndTimeMS() {
        val presenter = DefaultStatsPresenter()

        presenter(
            SearchResponse(
                hits = emptyList(),
                query = "",
                params = "",
                nbHits = 10,
                processingTimeMS = 10
            )
        ) shouldEqual "10 hits in 10ms"
    }
}
