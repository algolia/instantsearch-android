package stats

import com.algolia.instantsearch.stats.StatsPresenterImpl
import com.algolia.search.model.response.ResponseSearch
import shouldEqual
import kotlin.test.Test

class TestStatsPresenter {

    @Test
    fun responseNull() {
        val presenter = StatsPresenterImpl()

        presenter(null) shouldEqual ""
    }

    @Test
    fun responseEmpty() {
        val presenter = StatsPresenterImpl()

        presenter(ResponseSearch()) shouldEqual ""
    }

    @Test
    fun responseNbHits() {
        val presenter = StatsPresenterImpl()

        presenter(ResponseSearch(nbHitsOrNull = 10)) shouldEqual "10 hits"
    }

    @Test
    fun responseNbHitsAndTimeMS() {
        val presenter = StatsPresenterImpl()

        presenter(ResponseSearch(nbHitsOrNull = 10, processingTimeMSOrNull = 10)) shouldEqual "10 hits in 10ms"
    }
}
