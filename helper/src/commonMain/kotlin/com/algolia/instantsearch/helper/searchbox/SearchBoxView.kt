package com.algolia.instantsearch.helper.searchbox

/**
 * A View with which the user interacts to formulate a query.
 *
 * It is responsible for updating its change listener when the text changes,
 * and optionally its submit listener if it exposes a way for the user to submit that text.
 */
interface SearchBoxView {
    fun setTextChangeListener(listener: (String?) -> Unit)
    fun setTextSubmitListener(listener: (String?) -> Unit)
}