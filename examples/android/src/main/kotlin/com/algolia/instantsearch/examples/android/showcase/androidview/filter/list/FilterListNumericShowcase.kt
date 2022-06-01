package com.algolia.instantsearch.examples.android.showcase.androidview.filter.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.examples.android.databinding.HeaderFilterBinding
import com.algolia.instantsearch.examples.android.databinding.IncludeListBinding
import com.algolia.instantsearch.examples.android.databinding.ShowcaseFilterListBinding
import com.algolia.instantsearch.examples.android.showcase.androidview.client
import com.algolia.instantsearch.examples.android.showcase.androidview.configureRecyclerView
import com.algolia.instantsearch.examples.android.showcase.androidview.configureSearcher
import com.algolia.instantsearch.examples.android.showcase.androidview.configureToolbar
import com.algolia.instantsearch.examples.android.showcase.androidview.onClearAllThenClearFilters
import com.algolia.instantsearch.examples.android.showcase.androidview.onErrorThenUpdateFiltersText
import com.algolia.instantsearch.examples.android.showcase.androidview.onFilterChangedThenUpdateFiltersText
import com.algolia.instantsearch.examples.android.showcase.androidview.onResponseChangedThenUpdateNbHits
import com.algolia.instantsearch.examples.android.showcase.androidview.stubIndexName
import com.algolia.instantsearch.filter.list.FilterListConnector
import com.algolia.instantsearch.filter.list.connectView
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.groupOr
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator

class FilterListNumericShowcase : AppCompatActivity() {

    private val price = Attribute("price")
    private val groupPrice = groupOr(price)
    private val searcher = HitsSearcher(client, stubIndexName)
    private val filterState = FilterState()
    private val filters = listOf(
        Filter.Numeric(price, NumericOperator.Less, 5),
        Filter.Numeric(price, 5..10),
        Filter.Numeric(price, 10..25),
        Filter.Numeric(price, 25..100),
        Filter.Numeric(price, NumericOperator.Greater, 100)
    )
    private val filterList = FilterListConnector.Numeric(
        filters = filters,
        filterState = filterState,
        groupID = groupPrice,
        selectionMode = SelectionMode.Multiple
    )
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
