package com.algolia.instantsearch.showcase.filter.current

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.android.filter.current.FilterCurrentViewImpl
import com.algolia.instantsearch.filter.current.FilterCurrentConnector
import com.algolia.instantsearch.filter.current.connectView
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.filters
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.databinding.HeaderFilterBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseFilterCurrentBinding
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.NumericOperator

class ShowcaseFilterCurrent : AppCompatActivity() {

    private val color = Attribute("color")
    private val price = Attribute("price")
    private val tags = Attribute("_tags")
    private val groupColor = FilterGroupID(color)
    private val groupPrice = FilterGroupID(price)
    private val groupTags = FilterGroupID(tags)
    private val filters = filters {
        group(groupColor) {
            facet(color, "red")
            facet(color, "green")
        }
        group(groupTags) {
            tag("mobile")
        }
        group(groupPrice) {
            comparison(price, NumericOperator.NotEquals, 42)
            range(price, IntRange(0, 100))
        }
    }
    private val filterState = FilterState(filters)
    private val searcher = HitsSearcher(client, stubIndexName)
    private val currentFiltersColor = FilterCurrentConnector(filterState, listOf(groupColor))
    private val currentFiltersAll = FilterCurrentConnector(filterState)
    private val connection = ConnectionHandler(
        currentFiltersColor,
        currentFiltersAll,
        searcher.connectFilterState(filterState)
    )

    private lateinit var binding: ShowcaseFilterCurrentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ShowcaseFilterCurrentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val headerBinding = HeaderFilterBinding.bind(binding.headerFilter.root)

        connection += currentFiltersAll.connectView(FilterCurrentViewImpl(binding.chipGroupAll, R.layout.filter_chip))
        connection += currentFiltersColor.connectView(FilterCurrentViewImpl(binding.chipGroupColors, R.layout.filter_chip))

        configureSearcher(searcher)
        configureToolbar(binding.toolbar)
        onResetThenRestoreFilters(binding.reset, filterState, filters)
        onFilterChangedThenUpdateFiltersText(filterState, headerBinding.filtersTextView, color, price, tags)
        onErrorThenUpdateFiltersText(searcher, headerBinding.filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, headerBinding.nbHits, connection)
        onClearAllThenClearFilters(filterState, headerBinding.filtersClearAll, connection)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
