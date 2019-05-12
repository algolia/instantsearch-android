package com.algolia.instantsearch.helper.filter.list

import com.algolia.instantsearch.core.selectable.list.SelectableListViewModel
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.search.model.filter.Filter


sealed class FilterListViewModel<T : Filter>(
    items: List<T>,
    selected: Set<T>,
    selectionMode: SelectionMode
) : SelectableListViewModel<T, T>(items, selected, selectionMode) {

    public class Facet(
        items: List<Filter.Facet> = listOf(),
        selected: Set<Filter.Facet> = setOf(),
        selectionMode: SelectionMode = SelectionMode.Multiple
    ) : FilterListViewModel<Filter.Facet>(items, selected, selectionMode)

    public class Numeric(
        items: List<Filter.Numeric> = listOf(),
        selected: Set<Filter.Numeric> = setOf(),
        selectionMode: SelectionMode = SelectionMode.Single
    ) : FilterListViewModel<Filter.Numeric>(items, selected, selectionMode)

    public class Tag(
        items: List<Filter.Tag> = listOf(),
        selected: Set<Filter.Tag> = setOf(),
        selectionMode: SelectionMode = SelectionMode.Multiple
    ) : FilterListViewModel<Filter.Tag>(items, selected, selectionMode)

    public class All(
        items: List<Filter> = listOf(),
        selected: Set<Filter> = setOf(),
        selectionMode: SelectionMode = SelectionMode.Multiple
    ) : FilterListViewModel<Filter>(items, selected, selectionMode)
}