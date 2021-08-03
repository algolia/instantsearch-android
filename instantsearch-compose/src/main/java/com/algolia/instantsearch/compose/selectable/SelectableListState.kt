package com.algolia.instantsearch.compose.selectable

import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.core.selectable.list.SelectableListView

/**
 * [SelectableListView] for compose.
 */
public interface SelectableListState<T> : SelectableListView<T> {

    /**
     * List of selectable items.
     */
    public val items: List<SelectableItem<T>>
}
