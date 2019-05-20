package com.algolia.instantsearch.helper.searchbox

import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel


fun SearchBoxViewModel.connectView(searchBoxView: SearchBoxView) {
    searchBoxView.setTextChangeListener { newText ->
        query = newText
    }

    searchBoxView.setTextSubmitListener { newText ->
        query = newText
        submit()
    }
}