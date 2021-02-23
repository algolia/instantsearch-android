package com.algolia.instantsearch.core.smartsort

/**
 * Defines the way we want to interact with a Smart sort priority value.
 */
public typealias SmartSortPresenter<T> = (SmartSortPriority?) -> T
