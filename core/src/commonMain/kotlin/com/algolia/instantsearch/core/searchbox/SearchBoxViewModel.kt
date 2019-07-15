package com.algolia.instantsearch.core.searchbox

import com.algolia.instantsearch.core.observable.ObservableEvent
import com.algolia.instantsearch.core.observable.ObservableItem


public open class SearchBoxViewModel {

    public val query = ObservableItem<String?>(null)

    public val event = ObservableEvent<String?>()
}