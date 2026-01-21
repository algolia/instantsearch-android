package filter.numeric.comparison

import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.filter.numeric.comparison.setBoundsFromFacetStatsInt
import com.algolia.client.model.search.FacetStats
import shouldEqual
import kotlin.test.Test

class TestFilterComparisonComputeBounds {

    private val price = "price"
    private val facetStats = mapOf(
        price to FacetStats(min = 0.0, max = 10.0, sum = 10.0, avg = 5.0)
    )

    @Test
    fun setBoundsFromFacetStatsShouldUpdateBounds() {
        val viewModel = NumberViewModel<Int>()

        viewModel.setBoundsFromFacetStatsInt(price, facetStats)
        viewModel.bounds.value shouldEqual Range(0, 10)
    }
}
