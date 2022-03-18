package com.algolia.instantsearch.sortby

import com.algolia.instantsearch.core.selectable.map.SelectableMapViewModel
import com.algolia.search.model.IndexName

/**
 * Sort by view model with index selecting capabilities.
 */
public typealias SortByViewModel = SelectableMapViewModel<Int, IndexName>
