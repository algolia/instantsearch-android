package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.observable.ObservableEvent
import com.algolia.instantsearch.core.observable.ObservableItem


public open class LoadingViewModel(
    isLoading: Boolean = false
) {

    public val isLoading = ObservableItem(isLoading)
    public val eventReload = ObservableEvent<Unit>()
}