@file:JvmName("SelectableItem")

package com.algolia.instantsearch.core.selectable

import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.Connection
import kotlin.jvm.JvmName

/**
 * Connects this SelectableItemViewModel to a SelectableItemView,
 * updating it when the item or the selection changes.
 */
public fun <I, O> SelectableItemViewModel<I>.connectView(
    view: SelectableItemView<O>,
    presenter: Presenter<I, O>
): Connection {
    return SelectableItemConnectionView(this, view, presenter)
}