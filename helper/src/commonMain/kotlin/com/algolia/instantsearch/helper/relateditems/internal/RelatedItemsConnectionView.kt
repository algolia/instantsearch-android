package com.algolia.instantsearch.helper.relateditems.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.helper.relateditems.MatchingPattern
import com.algolia.instantsearch.helper.relateditems.internal.extensions.configureRelatedItems
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.indexing.Indexable
import com.algolia.search.model.response.ResponseSearch

internal data class RelatedItemsConnectionView<T>(
    private val searcher: SearcherSingleIndex,
    private val view: HitsView<T>,
    private val hit: T,
    private val matchingPatterns: List<MatchingPattern<T>>,
    private val presenter: Presenter<ResponseSearch, List<T>>
) : ConnectionImpl() where T : Indexable {

    init {
        searcher.configureRelatedItems(hit, matchingPatterns)
    }

    private val callback: Callback<ResponseSearch?> = { response ->
        if (response != null) {
            view.setHits(presenter(response))
        }
    }

    override fun connect() {
        super.connect()
        searcher.response.subscribe(callback)
    }

    override fun disconnect() {
        super.disconnect()
        searcher.response.unsubscribe(callback)
    }
}
