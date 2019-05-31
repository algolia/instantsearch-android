package com.algolia.instantsearch.helper.stats

import com.algolia.search.model.response.ResponseSearch


public typealias StatsPresenter<T> = (ResponseSearch?) -> T