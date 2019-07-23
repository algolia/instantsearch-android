package com.algolia.instantsearch.demo.filter.numeric.comparison

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.disconnect
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.numeric.comparison.FilterComparisonWidget
import com.algolia.instantsearch.helper.filter.numeric.comparison.setBoundsFromFacetStatsInt
import com.algolia.instantsearch.helper.filter.numeric.comparison.setBoundsFromFacetStatsLong
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.addFacet
import com.algolia.instantsearch.helper.searcher.with
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
    private val widgetPrice = FilterComparisonWidget<Long>(filterState, price, NumericOperator.GreaterOrEquals)
    private val widgetYear = FilterComparisonWidget<Int>(filterState, year, NumericOperator.Equals)
    private val connections = mutableListOf<Connection>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_comparison)

        searcher.query.addFacet(price)
        searcher.query.addFacet(year)

        val priceView = FilterPriceView(demoFilterComparison, price, widgetPrice.operator)
        val yearView = FilterYearView(demoFilterComparison, year, widgetYear.operator)

        connections += searcher.with(filterState)
        connections += widgetPrice.with(priceView)
        connections += widgetYear.with(yearView) { year -> year?.toString() ?: "" }

        configureToolbar(toolbar)
        configureSearcher(searcher)
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, price, year)
        onClearAllThenClearFilters(filterState, filtersClearAll)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)

        searcher.coroutineScope.launch {
            val response = searcher.search()

            response.facetStatsOrNull?.let {
                widgetPrice.viewModel.setBoundsFromFacetStatsLong(price, it)
                widgetYear.viewModel.setBoundsFromFacetStatsInt(year, it)
                withContext(Dispatchers.Main) {
                    inputHint.text = getInputHint(widgetYear.viewModel.bounds.value!!)
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
        widgetPrice.disconnect()
        widgetYear.disconnect()
        connections.disconnect()
    }
}