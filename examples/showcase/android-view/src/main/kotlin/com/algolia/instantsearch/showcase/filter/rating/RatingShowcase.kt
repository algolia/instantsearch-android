package com.algolia.instantsearch.showcase.filter.rating

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.filter.range.FilterRangeConnector
import com.algolia.instantsearch.filter.range.connectView
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.filters
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.databinding.HeaderFilterBinding
import com.algolia.instantsearch.showcase.databinding.IncludePlusMinusBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseFilterRatingBinding
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter

class RatingShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, IndexName("instant_search"))
    private val rating = Attribute("rating")
    private val groupID = FilterGroupID(rating)
    private val primaryBounds = 0f..5f
    private val initialRange = 3f..5f
    private val filters = filters {
        group(groupID) {
            +Filter.Numeric(
                rating,
                lowerBound = initialRange.start,
                upperBound = initialRange.endInclusive
            )
        }
    }
    private val filterState = FilterState(filters)
    private val range =
        FilterRangeConnector(filterState, rating, range = initialRange, bounds = primaryBounds)
    private val connection = ConnectionHandler(
        range,
        searcher.connectFilterState(filterState, Debouncer(100))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseFilterRatingBinding.inflate(layoutInflater)
        val headerBinding = HeaderFilterBinding.bind(binding.headerFilter.root)
        val buttonsBinding = IncludePlusMinusBinding.bind(binding.buttons.root)
        setContentView(binding.root)

        val ratingBarView = RatingBarView(binding.ratingBar).apply {
            stepSize = STEP
            buttonsBinding.plus.setOnClickListener { rating += stepSize }
            buttonsBinding.minus.setOnClickListener { rating -= stepSize }
        }
        connection += range.connectView(ratingBarView)
        connection += range.connectView(RatingTextView(binding.ratingLabel))

        configureToolbar(binding.toolbar)
        onFilterChangedThenUpdateFiltersText(filterState, headerBinding.filtersTextView, rating)
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

    companion object {
        private const val STEP = 0.1f
    }
}
