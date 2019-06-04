package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.event.connectView


public fun FilterClearViewModel.connectView(view: FilterClearView) {
   connectView(view) { click() }
}