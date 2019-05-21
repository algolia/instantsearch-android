package com.algolia.instantsearch.helper.searchbox

/**
 * A function that returns `true` if the given query should trigger a search request.
 */
typealias QueryHook = (String?) -> Boolean