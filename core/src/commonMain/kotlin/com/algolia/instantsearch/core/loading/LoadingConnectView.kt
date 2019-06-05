package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.event.connectView as connectEventView
import com.algolia.instantsearch.core.item.connectView as connectItemView


public fun LoadingViewModel.connectView(view: LoadingView) {
    connectItemView(view) { it }
    connectEventView(view) { trigger(Unit) }
}