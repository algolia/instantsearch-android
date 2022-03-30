package com.algolia.instantsearch.showcase.filter.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.filter.list.FilterListConnector
import com.algolia.instantsearch.filter.list.connectView
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.groupOr
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.databinding.HeaderFilterBinding
import com.algolia.instantsearch.showcase.databinding.IncludeListBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseFilterListBinding
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter

class FilterListTagShowcase : AppCompatActivity() {

    private val tags = Attribute("_tags")
    private val groupTags = groupOr(tags)
    private val filterState = FilterState()
    private val searcher = HitsSearcher(client, stubIndexName)
    private val filters = listOf(
        Filter.Tag("free shipping"),
        Filter.Tag("coupon"),
        Filter.Tag("free return"),
        Filter.Tag("on sale"),
        Filter.Tag("no exchange")
    )
    private val filterList = FilterListConnector.Tag(filters, filterState, groupID = groupTags)
    private val connection = ConnectionHandler(filterList, searcher.connectFilterState(filterState))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseFilterListBinding.inflate(layoutInflater)
        val listBinding = IncludeListBinding.bind(binding.list.root)
        val headerBinding = HeaderFilterBinding.bind(listBinding.headerFilter.root)
        setContentView(binding.root)

        val viewTag = FilterListAdapter<Filter.Tag>()

        connection += filterList.connectView(viewTag)

        configureToolbar(binding.toolbar)
        configureSearcher(searcher)
        configureRecyclerView(listBinding.listTopLeft, viewTag)
        onFilterChangedThenUpdateFiltersText(filterState, headerBinding.filtersTextView, tags)
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
