package com.algolia.instantsearch.demo.filter.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.list.FilterListViewModel
import com.algolia.instantsearch.helper.filter.list.connectFilterState
import com.algolia.instantsearch.helper.filter.list.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.header_filter.*
import kotlinx.android.synthetic.main.include_list.*


class FilterListTagDemo : AppCompatActivity() {

    private val tags = "tags"
    private val colors
        get() = mapOf(
            tags to ContextCompat.getColor(this, android.R.color.holo_green_dark)
        )
    private val tagFilters = listOf(
        Filter.Tag("free shipping"),
        Filter.Tag("coupon"),
        Filter.Tag("free return"),
        Filter.Tag("on sale"),
        Filter.Tag("no exchange")
    )
    private val groupIDTags = FilterGroupID.Or(tags)
    private val searcher = SearcherSingleIndex(stubIndex)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_list)

        searcher.index = client.initIndex(intent.indexName)

        val viewModelTag = FilterListViewModel.Tag(tagFilters)
        val viewTag = FilterListAdapter<Filter.Tag>()

        viewModelTag.connectFilterState(searcher.filterState, groupIDTags)
        viewModelTag.connectView(viewTag)

        configureRecyclerView(listTopLeft, viewTag)
        onFilterChangedThenUpdateFiltersText(searcher.filterState, colors, filtersTextView)
        onClearAllThenClearFilters(searcher.filterState, filtersClearAll)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)
        configureToolbar()

        searcher.search()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}