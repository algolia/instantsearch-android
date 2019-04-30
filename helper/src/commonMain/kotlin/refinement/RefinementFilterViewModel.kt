package refinement

import com.algolia.search.model.filter.Filter
import selection.SelectableViewModel


class RefinementFilterViewModel(val filter: Filter) : SelectableViewModel<Filter>(filter)
