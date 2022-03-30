package com.algolia.instantsearch.showcase.filter.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.android.filter.map.FilterMapViewRadioGroup
import com.algolia.instantsearch.filter.map.FilterMapConnector
import com.algolia.instantsearch.filter.map.connectView
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.groupAnd
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.databinding.HeaderFilterBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseFilterMapBinding
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter

class FilterMapShowcase : AppCompatActivity() {

    private val gender = Attribute("gender")
    private val groupGender = groupAnd(gender)
    private val filterState = FilterState()
    private val searcher = HitsSearcher(client, stubIndexName)
    private val filters = mapOf(
        R.id.male to Filter.Facet(gender, "male"),
        R.id.female to Filter.Facet(gender, "female")
    )
    private val filterMap = FilterMapConnector(filters, filterState, groupID = groupGender)
    private val connection = ConnectionHandler(filterMap, searcher.connectFilterState(filterState))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseFilterMapBinding.inflate(layoutInflater)
        val headerBinding = HeaderFilterBinding.bind(binding.headerFilter.root)
        setContentView(binding.root)

        val viewGender = FilterMapViewRadioGroup(binding.radioGroupGender)

        connection += filterMap.connectView(viewGender)

        configureToolbar(binding.toolbar)
        configureSearcher(searcher)
        onFilterChangedThenUpdateFiltersText(filterState, headerBinding.filtersTextView, gender)
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
