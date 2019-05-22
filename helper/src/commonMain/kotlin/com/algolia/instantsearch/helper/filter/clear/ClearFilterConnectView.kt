package com.algolia.instantsearch.helper.filter.clear

fun ClearFilterViewModel.connectView(view: ClearFilterView) {
    view.onClick = {
        onCleared.forEach { it() }
    }
}