package com.algolia.instantsearch.helper.filter.clear


public class ClearFiltersViewModel {

    public val onCleared: MutableList<() -> Unit> = mutableListOf()

    public fun clearFilters() {
        onCleared.forEach { it() }
    }
}