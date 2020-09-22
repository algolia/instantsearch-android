package com.algolia.instantsearch.core.selectable

import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.Connection

public fun <I, O> SelectableItemViewModel<I>.connectView(
    view: SelectableItemView<O>,
    presenter: Presenter<I, O>
): Connection {
    return SelectableItemConnectionView(this, view, presenter)
}
