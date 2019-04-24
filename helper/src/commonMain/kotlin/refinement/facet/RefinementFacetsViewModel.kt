package refinement.facet

import com.algolia.search.model.search.Facet
import selection.SelectionListViewModel
import selection.SelectionMode


class RefinementFacetsViewModel(
    selectionMode: SelectionMode = SelectionMode.Multiple
) : SelectionListViewModel<String, Facet>(selectionMode)