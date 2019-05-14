package com.algolia.instantsearch.helper.filter.list

import com.algolia.instantsearch.core.selectable.list.SelectableListViewModel
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.search.model.filter.Filter


sealed class FilterListViewModel<T : Filter>(
    items: List<T>,
    selectionMode: SelectionMode
) : SelectableListViewModel<T, T>(items, selectionMode) {

    public class Facet(
        items: List<Filter.Facet> = listOf(),
        selectionMode: SelectionMode = SelectionMode.Multiple
    ) : FilterListViewModel<Filter.Facet>(items, selectionMode)

    public class Numeric(
        items: List<Filter.Numeric> = listOf(),
        selectionMode: SelectionMode = SelectionMode.Single
    ) : FilterListViewModel<Filter.Numeric>(items, selectionMode)

    public class Tag(
        items: List<Filter.Tag> = listOf(),
        selectionMode: SelectionMode = SelectionMode.Multiple
    ) : FilterListViewModel<Filter.Tag>(items, selectionMode)

    public class All(
        items: List<Filter> = listOf(),
        selectionMode: SelectionMode = SelectionMode.Multiple
    ) : FilterListViewModel<Filter>(items, selectionMode)
}