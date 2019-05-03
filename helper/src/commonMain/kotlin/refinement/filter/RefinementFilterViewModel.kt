package refinement.filter

import com.algolia.search.model.filter.Filter
import selection.SelectableViewModel


public class RefinementFilterViewModel(filter: Filter) : SelectableViewModel<Filter>(filter)