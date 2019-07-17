package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.item.connectView


public fun LoadingViewModel.connectView(view: LoadingView) {
    isLoading.connectView(view) { it }
    view.onClick = (event::send)
}