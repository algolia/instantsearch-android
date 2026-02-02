package com.algolia.instantsearch.stats

import com.algolia.client.model.search.SearchResponse
import com.algolia.instantsearch.core.Presenter

public typealias StatsPresenter<T> = Presenter<SearchResponse?, T>
