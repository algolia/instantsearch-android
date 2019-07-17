package com.algolia.instantsearch.helper.android.list
private fun <T> LiveData<PagedList<T>>.connectFilterState(filterState: FilterState) {
    filterState.onChanged += {
        value?.dataSource?.invalidate()
    }
}