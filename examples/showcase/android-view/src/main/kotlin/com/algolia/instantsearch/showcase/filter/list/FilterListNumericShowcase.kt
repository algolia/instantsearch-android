package com.algolia.instantsearch.showcase.filter.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.filter.list.FilterListConnector
import com.algolia.instantsearch.filter.list.connectView
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.groupAnd
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.databinding.HeaderFilterBinding
import com.algolia.instantsearch.showcase.databinding.IncludeListBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseFilterListBinding
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator

class FilterListNumericShowcase : AppCompatActivity() {

    private val price = Attribute("price")
    private val groupPrice = groupAnd(price)
    private val searcher = HitsSearcher(client, stubIndexName)
    private val filterState = FilterState()
    private val filters = listOf(
        Filter.Numeric(price, NumericOperator.Less, 5),
        Filter.Numeric(price, 5..10),
        Filter.Numeric(price, 10..25),
        Filter.Numeric(price, 25..100),
        Filter.Numeric(price, NumericOperator.Greater, 100)
    )
    private val filterList = FilterListConnector.Numeric(filters, filterState, groupID = groupPrice)
    private val connection = ConnectionHandler(filterList, searcher.connectFilterState(filterState))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseFilterListBinding.inflate(layoutInflater)
        val listBinding = IncludeListBinding.bind(binding.list.root)
        val headerBinding = HeaderFilterBinding.bind(listBinding.headerFilter.root)
        setContentView(binding.root)

        val viewNumeric = FilterListAdapter<Filter.Numeric>()

        connection += searcher.connectFilterState(filterState)
        connection += filterList.connectView(viewNumeric)

        configureToolbar(binding.toolbar)
        configureSearcher(searcher)
        configureRecyclerView(listBinding.listTopLeft, viewNumeric)
        onFilterChangedThenUpdateFiltersText(filterState, headerBinding.filtersTextView, price)
        onClearAllThenClearFilters(filterState, headerBinding.filtersClearAll, connection)
        onErrorThenUpdateFiltersText(searcher, headerBinding.filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, headerBinding.nbHits, connection)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
