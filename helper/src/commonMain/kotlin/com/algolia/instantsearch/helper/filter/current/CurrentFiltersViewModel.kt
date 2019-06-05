package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.map.MapViewModel
import com.algolia.search.model.filter.Filter


public typealias CurrentFiltersViewModel = MapViewModel<String, Filter>

public val CurrentFiltersViewModel.filters: Set<Filter> get() = item.values.toSet()