package com.algolia.instantsearch.helper.index

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.Index


public class IndexSegmentWidget(
    public val searcher: SearcherSingleIndex,
    public val viewModel: IndexSegmentViewModel = IndexSegmentViewModel()
) : ConnectionImpl() {

    public constructor(
        indexes: Map<Int, Index>,
        searcher: SearcherSingleIndex,
        selected: Int? = null
    ) : this(searcher, IndexSegmentViewModel(indexes, selected))

    private val connectionSearcher = viewModel.connectionSearcher(searcher)

    override fun connect() {
        super.connect()
        connectionSearcher.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionSearcher.disconnect()
    }
}