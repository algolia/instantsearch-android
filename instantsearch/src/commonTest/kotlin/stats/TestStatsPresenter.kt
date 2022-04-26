package stats

import com.algolia.instantsearch.stats.DefaultStatsPresenter
import com.algolia.search.model.response.ResponseSearch
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

        presenter(ResponseSearch()) shouldEqual ""
    }

    @Test
    fun responseNbHits() {
        val presenter = DefaultStatsPresenter()

        presenter(ResponseSearch(nbHitsOrNull = 10)) shouldEqual "10 hits"
    }

    @Test
    fun responseNbHitsAndTimeMS() {
        val presenter = DefaultStatsPresenter()

        presenter(ResponseSearch(nbHitsOrNull = 10, processingTimeMSOrNull = 10)) shouldEqual "10 hits in 10ms"
    }
}
