package com.algolia.instantsearch.demo.filter.numeric.comparison

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.core.number.Range
import com.algolia.instantsearch.core.number.connectView
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.numeric.comparison.computeBoundsFromFacetStats
import com.algolia.instantsearch.helper.filter.numeric.comparison.connectFilterState
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_comparison)

        searcher.connectFilterState(filterState)
        searcher.query.addFacet(price)
        searcher.query.addFacet(year)

        val priceOperator = NumericOperator.GreaterOrEquals
        val priceViewModel = NumberViewModel.Int()
        val priceView = FilterPriceView(demoFilterComparison, price, priceOperator)

        priceViewModel.connectFilterState(price, priceOperator, filterState)
        priceViewModel.connectView(priceView)

        val yearOperator = NumericOperator.Equals
        val yearViewModel = NumberViewModel.Int()
        val yearView = FilterYearView(demoFilterComparison, year, yearOperator)

        yearViewModel.connectFilterState(year, yearOperator, filterState)
        yearViewModel.connectView(yearView) { number -> number?.toString() ?: "" }

        configureToolbar(toolbar)
        configureSearcher(searcher)
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, price, year)
        onClearAllThenClearFilters(filterState, filtersClearAll)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)

        searcher.coroutineScope.launch {
            val response = searcher.search()

            response.facetStatsOrNull?.let {
                priceViewModel.computeBoundsFromFacetStats(price, it)
                yearViewModel.computeBoundsFromFacetStats(year, it)
                withContext(Dispatchers.Main) {
                    inputHint.text =  getInputHint(yearViewModel.bounds!!)
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
    }
}