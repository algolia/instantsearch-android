@file:Suppress("FunctionName")

package com.algolia.instantsearch.helper.customdata

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex

/**
 * Component that displays custom data from rules.
 * [Documentation](https://www.algolia.com/doc/api-reference/widgets/query-rule-custom-data/android/)
 *
 * @param viewModel logic applied to the custom model
 * @param presenter defines the way we want to interact with a model
 * @param searcherConnection connection between the view model and a searcher
 */
public class QueryRuleCustomDataConnector<T> @PublishedApi internal constructor(
    public val viewModel: QueryRuleCustomDataViewModel<T>,
    private val searcherConnection: Connection,
    presenter: QueryRuleCustomDataPresenter<T>,
) : ConnectionImpl() {

    private val presenters: MutableSet<QueryRuleCustomDataPresenter<T>> = mutableSetOf(presenter)

    override fun connect() {
        super.connect()
        searcherConnection.connect()
        viewModel.item.subscribe(presenters)
    }

    override fun disconnect() {
        super.disconnect()
        searcherConnection.disconnect()
        viewModel.item.unsubscribe(presenters)
    }

    /**
     * Subscribe a presenter to the widget
     *
     * @param presenter defines the way we want to interact with a model
     */
    public fun subscribe(presenter: QueryRuleCustomDataPresenter<T>) {
        viewModel.item.subscribe(presenter)
        presenters += presenter
    }
}

/**
 * @param searcher searcher that handles your searches
 * @param viewModel logic applied to the custom model
 * @param presenter defines the way we want to interact with a model
 */
public inline fun <reified T> QueryRuleCustomDataConnector(
    searcher: SearcherSingleIndex,
    viewModel: QueryRuleCustomDataViewModel<T> = QueryRuleCustomDataViewModel(),
    noinline presenter: QueryRuleCustomDataPresenter<T>,
): QueryRuleCustomDataConnector<T> {
    return QueryRuleCustomDataConnector(viewModel, viewModel.connectSearcher(searcher), presenter)
}

/**
 * @param searcher searcher that handles your searches
 * @param queryIndex Index of query from response of which the user data will be extracted
 * @param viewModel logic applied to the custom model
 * @param presenter defines the way we want to interact with a model
 */
public inline fun <reified T> QueryRuleCustomDataConnector(
    searcher: SearcherMultipleIndex,
    queryIndex: Int,
    viewModel: QueryRuleCustomDataViewModel<T> = QueryRuleCustomDataViewModel(),
    noinline presenter: QueryRuleCustomDataPresenter<T>,
): QueryRuleCustomDataConnector<T> {
    return QueryRuleCustomDataConnector(viewModel, viewModel.connectSearcher(searcher, queryIndex), presenter)
}
