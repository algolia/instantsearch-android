package com.algolia.instantsearch.helper.android.list

import com.algolia.instantsearch.helper.filter.state.FilterState

/**
 * Connects a DataSourceFactory to a FilterState,
 * invalidating the former's [last DataSource][SearcherDataSourceFactory.lastDataSource]
 * when the [FilterState changes][FilterState.onChanged].
 */
public fun <T> SearcherSingleIndexDataSource.Factory<T>.connectFilterState(filterState: FilterState) {
    filterState.onChanged += {
        lastDataSource.invalidate()
    }
}