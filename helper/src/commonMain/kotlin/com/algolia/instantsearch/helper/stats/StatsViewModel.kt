package com.algolia.instantsearch.helper.stats

import com.algolia.instantsearch.core.observable.ObservableItem
import com.algolia.search.model.response.ResponseSearch


public open class StatsViewModel(response: ResponseSearch? = null) {

    val response = ObservableItem(response)
}