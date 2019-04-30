package com.algolia.instantsearch.sample.refinement.facet

import android.R
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.dsl.facets
import com.algolia.search.dsl.query
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import filter.FilterGroupID
import filter.FilterState
import filter.toFilterGroups
import highlight
import search.SearcherSingleIndex

abstract class RefinementActivity : AppCompatActivity() {

    protected val color = Attribute("color")
    protected val promotion = Attribute("promotion")
    protected val category = Attribute("category")

    private val query = query {
        facets {
            +color
            +promotion
            +category
        }
    }
    private val client = ClientSearch(
        ConfigurationSearch(
            ApplicationID("latency"),
            APIKey("1f6fd3a6fb973cb08419fe7d288fa4db")
        )
    )
    private val index = client.initIndex(IndexName("mobile_demo_refinement_facet"))
    private val filterState = FilterState(
        facetGroups = mutableMapOf(
            FilterGroupID.And(color.raw) to setOf(Filter.Facet(color, "green"))
        )
    )
    protected val searcher = SearcherSingleIndex(index, query, filterState = filterState)


    private val colors
        get() = mapOf(
            color.raw to ContextCompat.getColor(this, R.color.holo_red_dark),
            promotion.raw to ContextCompat.getColor(this, R.color.holo_blue_dark),
            category.raw to ContextCompat.getColor(this, R.color.holo_green_dark)
        )

    protected fun onChangeThenUpdateFiltersText(filtersTextView: TextView) {
        searcher.filterState.onChange += {
            filtersTextView.text = it.toFilterGroups().highlight(colors = colors)
        }
    }

    protected fun updateFiltersTextFromState(filtersTextView: TextView) {
        filtersTextView.text = searcher.filterState.toFilterGroups().highlight(colors = colors)
    }

    protected fun onClearAllThenClearFilters(filtersClearAll: View) {
        filtersClearAll.setOnClickListener {
            searcher.filterState.notify { clear() }
        }
    }

    protected fun onErrorThenUpdateFiltersText(filtersTextView: TextView) {
        searcher.errorListeners += {
            filtersTextView.text = it.localizedMessage
        }
    }
}