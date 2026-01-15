package com.algolia.instantsearch.index

import com.algolia.instantsearch.core.Presenter

/**
 * Presents indexName as [String].
 */
public typealias IndexNamePresenter = Presenter<String, String>

/**
 * Default implementation of [IndexNamePresenter].
 */
public object DefaultIndexPresenter : IndexNamePresenter {

    override fun invoke(indexName: String): String {
        return indexName
    }
}
