package refinement

import com.algolia.search.model.search.Facet
import selection.SelectableListViewModel
import selection.SelectionMode


class RefinementFacetsViewModel(
    selectionMode: SelectionMode = SelectionMode.Multiple
) : SelectableListViewModel<String, Facet>(selectionMode)