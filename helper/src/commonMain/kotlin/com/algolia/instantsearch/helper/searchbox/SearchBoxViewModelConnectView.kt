package com.algolia.instantsearch.helper.searchbox

import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel

/**
 * Connects a [SearchBoxView] to your SearchBoxViewModel, updating its [query][SearchBoxViewModel.query]
 * when the text changes or is submitted (then also calling [submit][SearchBoxViewModel.submit]).
 */
fun SearchBoxViewModel.connectView(searchBoxView: SearchBoxView) {
    searchBoxView.setTextChangeListener { newText ->
        query = newText
    }

    searchBoxView.setTextSubmitListener { newText ->
        query = newText
        submit()
    }
}