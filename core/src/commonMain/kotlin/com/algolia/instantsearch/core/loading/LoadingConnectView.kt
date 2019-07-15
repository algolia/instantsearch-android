package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.item.connectView
import com.algolia.instantsearch.core.observable.ObservableKey


public fun LoadingViewModel.connectView(view: LoadingView, key: ObservableKey? = null) {
    isLoading.connectView(view, key) { it }
    view.onClick = (event::send)
}