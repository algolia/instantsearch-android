package com.algolia.instantsearch.demo.filter.numeric.comparison

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.core.selectable.number.SelectableNumberViewModel
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.numeric.comparison.computeBoundsFromFacetStats
import com.algolia.instantsearch.helper.filter.numeric.comparison.connectFilterState
import com.algolia.instantsearch.helper.filter.numeric.comparison.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.addFacet
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.NumericOperator
import kotlinx.android.synthetic.main.demo_filter_comparison.*
import kotlinx.android.synthetic.main.header_filter.*
import kotlinx.coroutines.launch


class FilterComparisonDemo : AppCompatActivity() {

    private val price = Attribute("price")
    private val year = Attribute("year")
    private val colors
        get() = mapOf(
            price.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark),
            year.raw to ContextCompat.getColor(this, android.R.color.holo_blue_dark)
        )

    private val index = client.initIndex(IndexName("stub"))
    private val searcher = SearcherSingleIndex(index)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_comparison)

        searcher.query.addFacet(price)
        searcher.query.addFacet(year)
        searcher.index = client.initIndex(intent.indexName)

        val priceOperator = NumericOperator.GreaterOrEquals
        val priceViewModel = SelectableNumberViewModel.Int()
        val priceView = FilterPriceView(demoFilterComparison, price, priceOperator)

        priceViewModel.connectFilterState(price, priceOperator, searcher.filterState)
        priceViewModel.connectView(priceView)

        val yearOperator = NumericOperator.Equals
        val yearViewModel = SelectableNumberViewModel.Int()
        val yearView = FilterYearView(demoFilterComparison, year, yearOperator)

        yearViewModel.connectFilterState(year, yearOperator, searcher.filterState)
        yearViewModel.connectView(yearView)

        onChangeThenUpdateFiltersText(searcher.filterState, colors, filtersTextView)
        onClearAllThenClearFilters(searcher.filterState, filtersClearAll)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)
        configureToolbar()

        searcher.launch {
            searcher.search().join()
            println(searcher.response)
            searcher.response?.facetStatsOrNull?.let {
                priceViewModel.computeBoundsFromFacetStats(price, it)
                yearViewModel.computeBoundsFromFacetStats(year, it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}