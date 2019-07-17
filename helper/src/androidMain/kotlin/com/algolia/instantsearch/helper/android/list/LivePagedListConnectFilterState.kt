package com.algolia.instantsearch.helper.android.list

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.algolia.instantsearch.helper.filter.state.FilterState

public fun <T> LiveData<PagedList<T>>.connectFilterState(filterState: FilterState) {
    filterState.onChanged += {
        value?.dataSource?.invalidate()
    }
}