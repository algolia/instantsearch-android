package com.algolia.instantsearch.core.selectable

import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect


public fun <I, O> SelectableItemViewModel<I>.connectView(
    view: SelectableItemView<O>,
    presenter: Presenter<I, O>,
    connect: Boolean = true
): Connection {
    return SelectableItemConnectionView(this, view, presenter).autoConnect(connect)
}