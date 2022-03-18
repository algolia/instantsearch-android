package com.algolia.instantsearch.stats

import com.algolia.instantsearch.core.Presenter
import com.algolia.search.model.response.ResponseSearch

public typealias StatsPresenter<T> = Presenter<ResponseSearch?, T>
