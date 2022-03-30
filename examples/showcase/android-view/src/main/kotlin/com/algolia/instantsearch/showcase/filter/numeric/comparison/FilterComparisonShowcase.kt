package com.algolia.instantsearch.showcase.filter.numeric.comparison

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.filter.numeric.comparison.FilterComparisonConnector
import com.algolia.instantsearch.filter.numeric.comparison.connectView
import com.algolia.instantsearch.filter.numeric.comparison.setBoundsFromFacetStatsInt
import com.algolia.instantsearch.filter.numeric.comparison.setBoundsFromFacetStatsLong
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.searcher.addFacet
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.databinding.HeaderFilterBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseFilterComparisonBinding
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.NumericOperator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilterComparisonShowcase : AppCompatActivity() {

    private val price = Attribute("price")
    private val year = Attribute("year")
    private val filterState = FilterState()
    private val searcher =  HitsSearcher(client, stubIndexName)
    private val comparisonPrice = FilterComparisonConnector<Long>(filterState, price, NumericOperator.GreaterOrEquals)
    private val comparisonYear = FilterComparisonConnector<Int>(filterState, year, NumericOperator.Equals)
    private val connection = ConnectionHandler(
        comparisonPrice,
        comparisonYear,
        searcher.connectFilterState(filterState)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseFilterComparisonBinding.inflate(layoutInflater)
        val headerBinding = HeaderFilterBinding.bind(binding.headerFilter.root)
        setContentView(binding.root)

        searcher.query.addFacet(price)
        searcher.query.addFacet(year)

        val priceView = FilterPriceView(ShowcaseFilterComparisonBinding.bind(binding.filterComparison), price, comparisonPrice.operator)
        val yearView = FilterYearView(ShowcaseFilterComparisonBinding.bind(binding.filterComparison), year, comparisonYear.operator)

        connection += comparisonPrice.connectView(priceView)
        connection += comparisonYear.connectView(yearView) { year -> year?.toString() ?: "" }

        configureToolbar(binding.toolbar)
        configureSearcher(searcher)
        onFilterChangedThenUpdateFiltersText(filterState, headerBinding.filtersTextView, price, year)
        onClearAllThenClearFilters(filterState, headerBinding.filtersClearAll, connection)
        onErrorThenUpdateFiltersText(searcher, headerBinding.filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, headerBinding.nbHits, connection)

        searcher.coroutineScope.launch {
            val response = searcher.search()

            response.facetStatsOrNull?.let {
                comparisonPrice.viewModel.setBoundsFromFacetStatsLong(price, it)
                comparisonYear.viewModel.setBoundsFromFacetStatsInt(year, it)
                withContext(Dispatchers.Main) {
                    binding.inputHint.text = getInputHint(comparisonYear.viewModel.bounds.value!!)
                }
            }
        }
    }

    private fun getInputHint(bounds: Range<Int>): String {
        return getString(R.string.year_range, bounds.min, bounds.max)
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}