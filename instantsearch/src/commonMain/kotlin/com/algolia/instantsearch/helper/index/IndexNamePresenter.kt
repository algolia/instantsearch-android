package com.algolia.instantsearch.helper.index

import com.algolia.instantsearch.core.Presenter
import com.algolia.search.model.IndexName

/**
 * Presents [IndexName] as [String].
 */
public typealias IndexNamePresenter = Presenter<IndexName, String>

/**
 * Default implementation of [IndexNamePresenter].
 */
public object DefaultIndexPresenter : IndexNamePresenter {

    override fun invoke(indexName: IndexName): String {
        return indexName.raw
    }
}
