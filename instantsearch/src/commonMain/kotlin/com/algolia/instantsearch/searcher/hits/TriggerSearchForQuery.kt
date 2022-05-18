package com.algolia.instantsearch.searcher.hits

import com.algolia.search.model.search.Query

public fun interface TriggerSearchForQuery {

    public fun triggerFor(query: Query): Boolean
}
