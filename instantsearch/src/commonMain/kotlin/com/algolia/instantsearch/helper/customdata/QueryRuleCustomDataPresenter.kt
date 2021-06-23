package com.algolia.instantsearch.helper.customdata

/**
 * Defines the way we want to interact with a model.
 */
public typealias QueryRuleCustomDataPresenter<T, R> = (T?) -> R
