package com.algolia.instantsearch.examples.android.showcase.androidview.filter.clear

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.android.filter.clear.DefaultFilterClearView
import com.algolia.instantsearch.filter.clear.ClearMode
import com.algolia.instantsearch.filter.clear.FilterClearConnector
import com.algolia.instantsearch.filter.clear.connectView
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.filters
import com.algolia.instantsearch.filter.state.groupOr
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.examples.android.showcase.androidview.*
import com.algolia.instantsearch.examples.android.databinding.HeaderFilterBinding
import com.algolia.instantsearch.examples.android.databinding.ShowcaseFilterClearBinding
import com.algolia.search.model.Attribute

class FilterClearShowcase : AppCompatActivity() {

    private val color = String("color")
    private val category = String("category")
    private val groupColor = groupOr(color)
    private val groupCategory = groupOr(category)
    private val filters = filters {
        group(groupColor) {
            facet(color, "red")
            facet(color, "green")
        }
        group(groupCategory) {
            facet(category, "shoe")
        }
    }
    private val filterState = FilterState(filters)
    private val searcher = HitsSearcher(client, stubIndexName)
    private val clearAll = FilterClearConnector(filterState)
    private val clearSpecified =
        FilterClearConnector(filterState, listOf(groupColor), ClearMode.Specified)
    private val clearExcept =
        FilterClearConnector(filterState, listOf(groupColor), ClearMode.Except)
    private val connection = ConnectionHandler(
        clearSpecified,
        clearExcept,
        searcher.connectFilterState(filterState)
    )

    private lateinit var binding: ShowcaseFilterClearBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ShowcaseFilterClearBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val headerBinding = HeaderFilterBinding.bind(binding.headerFilter.root)

        connection += clearAll.connectView(DefaultFilterClearView(headerBinding.filtersClearAll))
        connection += clearSpecified.connectView(DefaultFilterClearView(binding.buttonClearSpecified))
        connection += clearExcept.connectView(DefaultFilterClearView(binding.buttonClearExcept))

        configureToolbar(binding.toolbar)
        configureSearcher(searcher)
        onFilterChangedThenUpdateFiltersText(filterState, headerBinding.filtersTextView, color, category)
        onErrorThenUpdateFiltersText(searcher, headerBinding.filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, headerBinding.nbHits, connection)
        onResetThenRestoreFilters(binding.reset, filterState, filters)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
