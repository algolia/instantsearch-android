package com.algolia.instantsearch.helper.filter.toggle

import com.algolia.instantsearch.core.selectable.SelectableItemViewModel
import com.algolia.search.model.filter.Filter


/**
 * A ViewModel storing a Filter that can be selected.
 */
public typealias FilterToggleViewModel = SelectableItemViewModel<Filter>