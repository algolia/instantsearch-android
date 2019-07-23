package com.algolia.instantsearch.helper.loading

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.asList
import com.algolia.instantsearch.core.connection.connect
import com.algolia.instantsearch.core.loading.LoadingView
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.loading.connectionView
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.connection.ConnectionImplWidget


public class LoadingWidget<R>(
    public val searcher: Searcher<R>,
    public val viewModel: LoadingViewModel = LoadingViewModel(),
    public val debouncer: Debouncer = Debouncer(200)
) : ConnectionImplWidget() {

    override val connections = viewModel
        .connectionSearcher(searcher, debouncer)
        .asList()
        .connect()

    public fun with(vararg views: LoadingView): List<Connection> {
        return views.map(viewModel::connectionView).connect()
    }
}