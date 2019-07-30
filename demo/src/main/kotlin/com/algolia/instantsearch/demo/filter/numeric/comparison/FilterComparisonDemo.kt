package com.algolia.instantsearch.demo.filter.numeric.comparison

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.numeric.comparison.FilterComparisonConnector
import com.algolia.instantsearch.helper.filter.numeric.comparison.connectView
import com.algolia.instantsearch.helper.filter.numeric.comparison.setBoundsFromFacetStatsInt
import com.algolia.instantsearch.helper.filter.numeric.comparison.setBoundsFromFacetStatsLong
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.addFacet
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.NumericOperator
import kotlinx.android.synthetic.main.demo_filter_comparison.*
import kotlinx.android.synthetic.main.header_filter.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FilterComparisonDemo : AppCompatActivity() {

    private val price = Attribute("price")
    private val year = Attribute("year")
    private val index = client.initIndex(IndexName("stub"))
    private val filterState = FilterState()
    private val searcher = SearcherSingleIndex(index)
    private val comparisonPrice = FilterComparisonConnector<Long>(filterState, price, NumericOperator.GreaterOrEquals)
    private val comparisonYear = FilterComparisonConnector<Int>(filterState, year, NumericOperator.Equals)
    private val connection = ConnectionHandler(
        comparisonPrice,
        comparisonYear,
        searcher.connectFilterState(filterState)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_comparison)

        searcher.query.addFacet(price)
        searcher.query.addFacet(year)

        val priceView = FilterPriceView(demoFilterComparison, price, comparisonPrice.operator)
        val yearView = FilterYearView(demoFilterComparison, year, comparisonYear.operator)

        connection += comparisonPrice.connectView(priceView)
        connection += comparisonYear.connectView(yearView) { year -> year?.toString() ?: "" }

        configureToolbar(toolbar)
        configureSearcher(searcher)
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, price, year)
        onClearAllThenClearFilters(filterState, filtersClearAll, connection)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits, connection)

        searcher.coroutineScope.launch {
            val response = searcher.search()

            response.facetStatsOrNull?.let {
                comparisonPrice.viewModel.setBoundsFromFacetStatsLong(price, it)
                comparisonYear.viewModel.setBoundsFromFacetStatsInt(year, it)
                withContext(Dispatchers.Main) {
                    inputHint.text = getInputHint(comparisonYear.viewModel.bounds.value!!)
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
        connection.disconnect()
    }
}