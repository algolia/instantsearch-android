package com.algolia.instantsearch.compose.filter.list.internal

import com.algolia.instantsearch.compose.filter.list.FilterListState
import com.algolia.instantsearch.compose.internal.trace
import com.algolia.instantsearch.compose.selectable.list.SelectableListState
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.search.model.filter.Filter

/**
 * [FilterListState] implementation.
 *
 * @param items initial filters list items
 */
internal class FilterListStateImpl<T : Filter>(
    items: List<SelectableItem<T>>
) : FilterListState<T>, SelectableListState<T> by SelectableListState(items) {

    init {
        trace()
    }
}
