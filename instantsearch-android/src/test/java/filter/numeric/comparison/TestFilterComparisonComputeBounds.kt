package filter.numeric.comparison

import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.helper.filter.numeric.comparison.setBoundsFromFacetStatsInt
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.FacetStats
import shouldEqual
import kotlin.test.Test

class TestFilterComparisonComputeBounds {

    private val price = Attribute("price")
    private val facetStats = mapOf(
        price to FacetStats(min = 0f, max = 10f, sum = 10f, average = 5f)
    )

    @Test
    fun setBoundsFromFacetStatsShouldUpdateBounds() {
        val viewModel = NumberViewModel<Int>()

        viewModel.setBoundsFromFacetStatsInt(price, facetStats)
        viewModel.bounds.value shouldEqual Range(0, 10)
    }
}
