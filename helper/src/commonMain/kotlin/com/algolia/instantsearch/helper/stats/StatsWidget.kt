package com.algolia.instantsearch.helper.stats

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.response.ResponseSearch


public class StatsWidget(
    public val searcher: SearcherSingleIndex,
    public val viewModel: StatsViewModel = StatsViewModel()
) : ConnectionImpl() {

    public constructor(
        searcher: SearcherSingleIndex,
        responseSearch: ResponseSearch
    ) : this(searcher, StatsViewModel(responseSearch))

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