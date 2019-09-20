package com.algolia.instantsearch.helper.sortby

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.Index
import kotlin.jvm.JvmOverloads

/**
 * @see connectSearcher
 */
public data class SortByConnector @JvmOverloads constructor(
    public val searcher: SearcherSingleIndex,
    public val viewModel: SortByViewModel = SortByViewModel()
) : ConnectionImpl() {

    public constructor(
        indexes: Map<Int, Index>,
        searcher: SearcherSingleIndex,
        selected: Int? = null
    ) : this(searcher, SortByViewModel(indexes, selected))

    private val connectionSearcher = viewModel.connectSearcher(searcher)

    override fun connect() {
        super.connect()
        connectionSearcher.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionSearcher.disconnect()
    }
}