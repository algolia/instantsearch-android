package com.algolia.instantsearch.demo.filter.toggle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.selectable.SelectableCompoundButton
import com.algolia.instantsearch.helper.filter.toggle.FilterToggleViewModel
import com.algolia.instantsearch.helper.filter.toggle.connectFilterState
import com.algolia.instantsearch.helper.filter.toggle.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.demo_filter_toggle_default.*
import kotlinx.android.synthetic.main.header_filter.*


class FilterToggleDefault : AppCompatActivity() {

    private val popular = Attribute("popular")
    private val colors
        get() = mapOf(popular.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark))

    private val index = client.initIndex(IndexName("stub"))
    private val searcher = SearcherSingleIndex(index)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_toggle)

        searcher.index = client.initIndex(intent.indexName)

        val viewModelPopular = FilterToggleViewModel(Filter.Facet(popular, true))
        val viewPopular = SelectableCompoundButton(checkBoxPopular)

        viewModelPopular.connectFilterState(searcher.filterState, default = Filter.Facet(popular, false))
        viewModelPopular.connectView(viewPopular)

        onChangeThenUpdateFiltersText(searcher.filterState, colors, filtersTextView)
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