package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.map.MapViewModel
import com.algolia.search.model.filter.Filter


public typealias FilterCurrentViewModel = MapViewModel<String, Filter>

public val FilterCurrentViewModel.filters: Set<Filter> get() = map.get().values.toSet()