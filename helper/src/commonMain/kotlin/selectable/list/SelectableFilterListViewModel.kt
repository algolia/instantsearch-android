package selectable.list

import com.algolia.search.model.filter.Filter


sealed class SelectableFilterListViewModel<T : Filter>(
    items: List<T>,
    selected: Set<T>,
    selectionMode: SelectionMode
) : SelectableListViewModel<T, T>(items, selected, selectionMode) {

    public class Facet(
        items: List<Filter.Facet> = listOf(),
        selected: Set<Filter.Facet> = setOf(),
        selectionMode: SelectionMode = SelectionMode.Multiple
    ) : SelectableFilterListViewModel<Filter.Facet>(items, selected, selectionMode)

    public class Numeric(
        items: List<Filter.Numeric> = listOf(),
        selected: Set<Filter.Numeric> = setOf(),
        selectionMode: SelectionMode = SelectionMode.Single
    ) : SelectableFilterListViewModel<Filter.Numeric>(items, selected, selectionMode)

    public class Tag(
        items: List<Filter.Tag> = listOf(),
        selected: Set<Filter.Tag> = setOf(),
        selectionMode: SelectionMode = SelectionMode.Multiple
    ) : SelectableFilterListViewModel<Filter.Tag>(items, selected, selectionMode)

    public class All(
        items: List<Filter> = listOf(),
        selected: Set<Filter> = setOf(),
        selectionMode: SelectionMode = SelectionMode.Multiple
    ) : SelectableFilterListViewModel<Filter>(items, selected, selectionMode)
}