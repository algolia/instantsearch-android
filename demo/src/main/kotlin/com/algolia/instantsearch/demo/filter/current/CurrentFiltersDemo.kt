package com.algolia.instantsearch.demo.filter.current

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.filter.current.CurrentFiltersViewImpl
import com.algolia.instantsearch.helper.filter.current.CurrentFiltersViewModel
import com.algolia.instantsearch.helper.filter.current.connectFilterState
import com.algolia.instantsearch.helper.filter.current.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.filters
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.NumericOperator
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.demo_filter_current.*
import kotlinx.android.synthetic.main.header_filter.*

class CurrentFiltersDemo : AppCompatActivity() {

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
    private val searcher = SearcherSingleIndex(stubIndex)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_current)

        searcher.connectFilterState(filterState)

        val viewModelColors = CurrentFiltersViewModel()
        val viewColors = CurrentFiltersViewImpl(chipGroupColors as ChipGroup, R.layout.filter_chip)
        viewModelColors.connectFilterState(filterState, groupColor)
        viewModelColors.connectView(viewColors)

        val viewModelAll = CurrentFiltersViewModel()
        val viewAll = CurrentFiltersViewImpl(chipGroupAll as ChipGroup, R.layout.filter_chip)
        viewModelAll.connectFilterState(filterState)
        viewModelAll.connectView(viewAll)

        configureSearcher(searcher)
        configureToolbar(toolbar)
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, color, price, tags)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)
        onClearAllThenClearFilters(filterState, filtersClearAll)
        onResetThenRestoreFilters(reset, filterState, filters)

        searcher.searchAsync()
    }
}