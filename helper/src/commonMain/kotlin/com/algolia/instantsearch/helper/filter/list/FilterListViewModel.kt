package com.algolia.instantsearch.helper.filter.list

import com.algolia.instantsearch.core.selectable.list.SelectableListViewModel
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.search.model.filter.Filter
import kotlin.jvm.JvmField
import kotlin.jvm.JvmOverloads

/**
 * A ViewModel storing a list of filters, that can be selected according to a [mode][SelectionMode].
 */
public sealed class FilterListViewModel<T : Filter>(
    items: List<T>,
    selectionMode: SelectionMode
) : SelectableListViewModel<T, T>(items, selectionMode) {

    /**
     * A [FilterListViewModel] holding [facet filters][Filter.Facet].
     */
    public class Facet @JvmOverloads constructor(
        items: List<Filter.Facet> = listOf(),
        selectionMode: SelectionMode = SelectionMode.Multiple
    ) : FilterListViewModel<Filter.Facet>(items, selectionMode)

    /**
     * A [FilterListViewModel] holding [numeric filters][Filter.Numeric].
     */
    public class Numeric @JvmOverloads constructor(
        items: List<Filter.Numeric> = listOf(),
        selectionMode: SelectionMode = SelectionMode.Single
    ) : FilterListViewModel<Filter.Numeric>(items, selectionMode)

    /**
     * A [FilterListViewModel] holding [tag filters][Filter.Tag].
     */
    public class Tag @JvmOverloads constructor(
        items: List<Filter.Tag> = listOf(),
        selectionMode: SelectionMode = SelectionMode.Multiple
    ) : FilterListViewModel<Filter.Tag>(items, selectionMode)

    /**
     * A [FilterListViewModel] holding [filters][Filter].
     */
    public class All @JvmOverloads constructor(
        items: List<Filter> = listOf(),
        selectionMode: SelectionMode = SelectionMode.Multiple
    ) : FilterListViewModel<Filter>(items, selectionMode)
}