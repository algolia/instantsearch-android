package com.algolia.instantsearch.demo.filter.segment

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.selectable.SelectableRadioGroup
import com.algolia.instantsearch.helper.filter.segment.FilterSegmentViewModel
import com.algolia.instantsearch.helper.filter.segment.connectFilterState
import com.algolia.instantsearch.helper.filter.segment.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.demo_filter_segment.*
import kotlinx.android.synthetic.main.header_filter.*


class FilterSegmentDemo : DemoActivity() {

    private val gender = Attribute("gender")
    private val colors
        get() = mapOf(
            gender.raw to ContextCompat.getColor(this, android.R.color.holo_orange_light)
        )

    private val groupID = FilterGroupID.And(gender)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_segment)

        val viewModelGender = FilterSegmentViewModel(
            items = mapOf(
                R.id.male to Filter.Facet(gender, "male"),
                R.id.female to Filter.Facet(gender, "female")
            )
        )
        val viewGender = SelectableRadioGroup(radioGroupGender)

        viewModelGender.connectFilterState(searcher.filterState, groupID)
        viewModelGender.connectView(viewGender)

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