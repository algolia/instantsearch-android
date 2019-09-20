package com.algolia.instantsearch.helper.searchbox

/**
 * A Mode of operation for search query changes.
 */
enum class SearchMode {
    /**
     * Trigger a request on every keystroke.
     */
    AsYouType,
    /**
     * Trigger a request only when the query is submitted.
     */
    OnSubmit
}