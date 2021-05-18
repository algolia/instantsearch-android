package com.algolia.instantsearch.helper.filter.current.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.current.FilterAndID
import com.algolia.instantsearch.helper.filter.current.FilterCurrentPresenter
import com.algolia.instantsearch.helper.filter.current.FilterCurrentPresenterImpl
import com.algolia.instantsearch.helper.filter.current.FilterCurrentView
import com.algolia.instantsearch.helper.filter.current.FilterCurrentViewModel
import com.algolia.search.model.filter.Filter

internal data class FilterCurrentConnectionView(
    private val viewModel: FilterCurrentViewModel,
    private val view: FilterCurrentView,
    private val presenter: FilterCurrentPresenter = FilterCurrentPresenterImpl(),
) : ConnectionImpl() {

    private val updateFilters: Callback<Map<FilterAndID, Filter>> = { filters ->
        view.setFilters(presenter(filters))
    }

    override fun connect() {
        super.connect()
        viewModel.map.subscribePast(updateFilters)
        view.onFilterSelected = (viewModel::remove)
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.map.unsubscribe(updateFilters)
        view.onFilterSelected = null
    }
}
