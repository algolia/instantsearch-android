package com.algolia.instantsearch.core.selectable

import com.algolia.instantsearch.core.observable.ObservableEvent
import com.algolia.instantsearch.core.observable.ObservableItem


public open class SelectableItemViewModel<T>(
    item: T
) {

    public val item = ObservableItem(item)
    public val isSelected = ObservableItem(false)
    public val eventSelection = ObservableEvent<Boolean>()
}