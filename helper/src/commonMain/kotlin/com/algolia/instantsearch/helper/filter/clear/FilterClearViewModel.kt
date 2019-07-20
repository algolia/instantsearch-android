package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.observable.ObservableEvent


public open class FilterClearViewModel {

    public val eventClear = ObservableEvent<Unit>()
}