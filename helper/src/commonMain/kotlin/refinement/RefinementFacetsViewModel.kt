package refinement

import com.algolia.search.model.search.Facet


public class RefinementFacetsViewModel(
    selectionMode: SelectionMode = SelectionMode.SingleChoice
) : RefinementListViewModel<String, Facet>(selectionMode)