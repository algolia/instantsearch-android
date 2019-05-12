package selectable.numeric

import com.algolia.search.model.filter.Filter
import selectable.list.SelectableListViewModel
import selectable.list.SelectionMode


public class SelectableNumericsViewModel(
    items: List<Filter.Numeric> = listOf(),
    selected: Filter.Numeric? = null
) : SelectableListViewModel<Filter.Numeric, Filter.Numeric>(
    items = items,
    selections = if (selected != null) setOf(selected) else setOf(),
    selectionMode = SelectionMode.Single
)