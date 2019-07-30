package com.algolia.instantsearch.demo.filter.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.list.FilterListConnector
import com.algolia.instantsearch.helper.filter.list.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.groupOr
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.demo_filter_list.*
import kotlinx.android.synthetic.main.header_filter.*
import kotlinx.android.synthetic.main.include_list.*


class FilterListTagDemo : AppCompatActivity() {

    private val tags = Attribute("_tags")
    private val groupTags = groupOr(tags)
    private val filterState = FilterState()
    private val searcher = SearcherSingleIndex(stubIndex)
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
        setContentView(R.layout.demo_filter_list)

        val viewTag = FilterListAdapter<Filter.Tag>()

        connection += filterList.connectView(viewTag)

        configureToolbar(toolbar)
        configureSearcher(searcher)
        configureRecyclerView(listTopLeft, viewTag)
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, tags)
        onClearAllThenClearFilters(filterState, filtersClearAll, connection)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits, connection)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.disconnect()
    }
}