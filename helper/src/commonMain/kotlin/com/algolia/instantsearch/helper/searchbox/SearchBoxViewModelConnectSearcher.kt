package com.algolia.instantsearch.helper.searchbox

import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.helper.searcher.Searcher

/**
 * Connects a [Searcher] to your SearchBoxViewModel, triggering a search on each new query.
 * @param searcher the searcher to connect.
 * @param searchAsYouType when `false`, the search is only triggered on [submit][SearchBoxViewModel.submit].
 * @param queryHook a function that will receive each new query and returns `true` when a trigger should be sent.
 */
inline fun SearchBoxViewModel.connectSearcher(
    searcher: Searcher,
    searchAsYouType: Boolean = true,
    crossinline queryHook: QueryHook = { _ -> true }
) {
    val triggerSearch: (String?) -> Unit = {
        searcher.setQuery(it)
        if (queryHook(it)) {
            searcher.search()
        }
    }
    if (searchAsYouType) {
        onChanged = onChanged + triggerSearch
    } else {
        onSubmitted = onSubmitted + triggerSearch
    }
}