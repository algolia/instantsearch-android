package com.algolia.instantsearch.index

import com.algolia.search.client.Index

public object IndexPresenterImpl : IndexPresenter {

    override fun invoke(index: Index): String {
        return index.indexName.raw
    }
}
