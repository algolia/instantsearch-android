package refinement.filter

import com.algolia.search.model.filter.Filter
import selection.map.SelectableMapViewModel


public class RefinementFiltersViewModel(
    filters: Map<Int, Filter>,
    selected: Int? = null
): SelectableMapViewModel<Int, Filter>(filters, selected)