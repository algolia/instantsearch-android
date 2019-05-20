package com.algolia.instantsearch.helper.searchbox

interface SearchBoxView {
    fun setTextChangeListener(listener: (String?) -> Unit)
    fun setTextSubmitListener(listener: (String?) -> Unit)
}