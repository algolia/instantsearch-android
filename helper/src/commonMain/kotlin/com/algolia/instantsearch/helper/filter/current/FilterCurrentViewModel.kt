package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.map.MapViewModel
import com.algolia.search.model.filter.Filter

/**
 * A ViewModel storing currently active filters, with which users can interact.
 */
public class FilterCurrentViewModel(items: Map<FilterAndID, Filter> = mapOf()) :
    MapViewModel<FilterAndID, Filter>(items)