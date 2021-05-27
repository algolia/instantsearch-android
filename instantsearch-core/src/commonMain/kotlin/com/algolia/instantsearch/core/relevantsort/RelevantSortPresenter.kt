package com.algolia.instantsearch.core.relevantsort

/**
 * Defines the way we want to interact with a Relevant sort priority value.
 */
public typealias RelevantSortPresenter<T> = (RelevantSortPriority?) -> T
