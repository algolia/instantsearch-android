package com.algolia.instantsearch.helper.sortby

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.extension.traceSortByConnector
import com.algolia.instantsearch.helper.searcher.SearcherIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.Index

/**
 * SortBy displays a list of indices, allowing a user to change the way hits are sorted (using replica indices).
 * Another common use case is to let the user switch between different indices to show different results.
 * For this to work, you must define all indices that you pass to SortBy as replicas of the main index.
 * [Documentation](https://www.algolia.com/doc/api-reference/widgets/sort-by/android/)
 *
 * @param searcher the Searcher that handles your searches
 * @param viewModel the logic applied to the index sorting/switching
 */
@Deprecated("use SortByConnector with IndexName instead", ReplaceWith("com.algolia.instantsearch.helper.sortby.searcher.SortByConnector"))
public data class SortByConnector(
    public val searcher: SearcherSingleIndex,
    public val viewModel: SortByViewModel = SortByViewModel(),
) : ConnectionImpl() {

    /**
     * @param indexes the list of indices to search in
     * @param searcher the Searcher that handles your searches
     * @param selected the index to select. By default, none is selected.
     */
    public constructor(
        indexes: Map<Int, Index>,
        searcher: SearcherSingleIndex,
        selected: Int? = null,
    ) : this(searcher, SortByViewModel(indexes, selected))

    private val connectionSearcher = viewModel.connectSearcher(searcher)

    init {
        traceSortByConnector()
    }

    override fun connect() {
        super.connect()
        connectionSearcher.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionSearcher.disconnect()
    }
}
