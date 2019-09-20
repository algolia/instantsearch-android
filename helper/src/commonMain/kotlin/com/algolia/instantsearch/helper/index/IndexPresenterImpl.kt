package com.algolia.instantsearch.helper.index

import com.algolia.search.client.Index


public object IndexPresenterImpl : IndexPresenter {

    override fun invoke(index: Index): String {
        return present(index)
    }

    fun present(index: Index) = index.indexName.raw
}