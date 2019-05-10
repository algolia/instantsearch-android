package refinement.facet

import com.algolia.search.model.search.Facet
import selection.list.SelectableListViewModel
import selection.list.SelectionMode


public class RefinementFacetsViewModel(
    items: List<Facet> = listOf(),
    selections: Set<String> = setOf(),
    selectionMode: SelectionMode = SelectionMode.Multiple
) : SelectableListViewModel<String, Facet>(items, selections, selectionMode)