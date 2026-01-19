package com.algolia.instantsearch.filter.list

import com.algolia.instantsearch.core.selectable.list.SelectableListViewModel
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.extension.traceFacetFilterList
import com.algolia.instantsearch.extension.traceFilterList
import com.algolia.instantsearch.extension.traceNumericFilterList
import com.algolia.instantsearch.extension.traceTagFilterList
import com.algolia.instantsearch.filter.Filter

public sealed class FilterListViewModel<T : Filter>(
    items: List<T>,
    selectionMode: SelectionMode,
) : SelectableListViewModel<T, T>(items, selectionMode) {

    public class Facet(
        items: List<Filter.Facet> = emptyList(),
        selectionMode: SelectionMode = SelectionMode.Multiple,
    ) : FilterListViewModel<Filter.Facet>(items, selectionMode) {
        init {
            traceFacetFilterList()
        }
    }

    public class Numeric(
        items: List<Filter.Numeric> = emptyList(),
        selectionMode: SelectionMode = SelectionMode.Single,
    ) : FilterListViewModel<Filter.Numeric>(items, selectionMode) {
        init {
            traceNumericFilterList()
        }
    }

    public class Tag(
        items: List<Filter.Tag> = emptyList(),
        selectionMode: SelectionMode = SelectionMode.Multiple,
    ) : FilterListViewModel<Filter.Tag>(items, selectionMode) {
        init {
            traceTagFilterList()
        }
    }

    public class All(
        items: List<Filter> = emptyList(),
        selectionMode: SelectionMode = SelectionMode.Multiple,
    ) : FilterListViewModel<Filter>(items, selectionMode) {
        init {
            traceFilterList()
        }
    }
}
