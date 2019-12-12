package com.algolia.instantsearch.helper.android.insights

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.facet.FacetListViewModel
import com.algolia.instantsearch.helper.insights.TrackerInsights
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.insights.EventName


internal class InsightsConnection(
    private val trackerInsights: TrackerInsights,
    private val facetListViewModel: FacetListViewModel,
    private val eventName: EventName,
    private val attribute: Attribute
) : ConnectionImpl() {

    private val clickFilters: Callback<Set<String>> = { selections ->
        val filters = selections.map { Filter.Facet(attribute, it) }

        trackerInsights.clickedFilters(eventName, filters)
    }

    override fun connect() {
        super.connect()
        facetListViewModel.eventSelection.subscribe(clickFilters)

    }

    override fun disconnect() {
        super.disconnect()
        facetListViewModel.eventSelection.unsubscribe(clickFilters)
    }
}