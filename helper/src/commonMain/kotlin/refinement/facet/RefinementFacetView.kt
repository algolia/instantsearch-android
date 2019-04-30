package refinement.facet

import com.algolia.search.model.search.Facet
import selection.SelectableView


//TODO: Try with SelectableView<Filter.*> and see down the route
public typealias RefinementFacetView = SelectableView<Facet>
