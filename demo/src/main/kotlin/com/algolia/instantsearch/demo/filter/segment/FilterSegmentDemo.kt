package com.algolia.instantsearch.demo.filter.segment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.selectable.SelectableSegmentViewRadioGroup
import com.algolia.instantsearch.helper.filter.segment.FilterSegmentViewModel
import com.algolia.instantsearch.helper.filter.segment.connectFilterState
import com.algolia.instantsearch.helper.filter.segment.connectView
import com.algolia.instantsearch.helper.filter.state.groupAnd
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.demo_filter_segment.*
import kotlinx.android.synthetic.main.header_filter.*


class FilterSegmentDemo : AppCompatActivity() {

    private val gender = Attribute("gender")
    private val groupGender = groupAnd(gender)
    private val searcher = SearcherSingleIndex(stubIndex)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_segment)

        val viewModelGender = FilterSegmentViewModel(
            items = mapOf(
                R.id.male to Filter.Facet(gender, "male"),
                R.id.female to Filter.Facet(gender, "female")
            )
        )
        val viewGender = SelectableSegmentViewRadioGroup(radioGroupGender)

        viewModelGender.connectFilterState(searcher.filterState, groupGender)
        viewModelGender.connectView(viewGender)

        configureToolbar(toolbar)
        configureSearcher(searcher)
        onFilterChangedThenUpdateFiltersText(searcher.filterState, filtersTextView, gender)
        onClearAllThenClearFilters(searcher.filterState, filtersClearAll)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)

        searcher.search()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}