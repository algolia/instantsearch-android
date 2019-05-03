package refinement.facet

import com.algolia.search.model.search.Facet
import selection.list.SelectableListViewModel
import selection.list.SelectionMode


class RefinementFacetsViewModel(
    selectionMode: SelectionMode = SelectionMode.Multiple
) : SelectableListViewModel<String, Facet>(selectionMode)