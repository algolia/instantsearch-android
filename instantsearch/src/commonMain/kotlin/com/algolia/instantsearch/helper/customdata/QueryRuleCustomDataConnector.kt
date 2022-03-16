@file:Suppress("FunctionName", "DEPRECATION")

package com.algolia.instantsearch.helper.customdata

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.extension.traceQueryRuleCustomDataConnector
import com.algolia.instantsearch.helper.searcher.SearcherForHits
import kotlinx.serialization.DeserializationStrategy

/**
 * Component that displays custom data from rules.
 * [Documentation](https://www.algolia.com/doc/api-reference/widgets/query-rule-custom-data/android/)
 *
 * @param viewModel logic applied to the custom model
 * @param searcherConnection connection between the view model and a searcher
 */
public class QueryRuleCustomDataConnector<T> @PublishedApi internal constructor(
    public val viewModel: QueryRuleCustomDataViewModel<T>,
    private val searcherConnection: Connection,
) : ConnectionImpl() {

    private val presenters: MutableSet<QueryRuleCustomDataPresenter<T>> = mutableSetOf()

    init {
        traceQueryRuleCustomDataConnector()
    }

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

// region function builders

/**
 * Create an instance of [QueryRuleCustomDataConnector]
 *
 * @param searcher searcher that handles your searches
 * @param deserializer deserializes the model into a value of type T
 * @param initialItem initial item
 * @param presenter defines the way we want to interact with a model
 */
public fun <T> QueryRuleCustomDataConnector(
    searcher: SearcherForHits<*>,
    deserializer: DeserializationStrategy<T>,
    initialItem: T? = null,
    presenter: QueryRuleCustomDataPresenter<T>? = null,
): QueryRuleCustomDataConnector<T> {
    val viewModel = QueryRuleCustomDataViewModel(deserializer, initialItem)
    return QueryRuleCustomDataConnector(viewModel, viewModel.connectSearcher(searcher)).also {
        if (presenter != null) it.subscribe(presenter)
    }
}

/**
 * Create an instance of [QueryRuleCustomDataConnector].
 *
 * @param searcher searcher that handles your searches
 * @param viewModel logic applied to the custom model
 * @param presenter defines the way we want to interact with a model
 */
public inline fun <reified T> QueryRuleCustomDataConnector(
    searcher: SearcherForHits<*>,
    viewModel: QueryRuleCustomDataViewModel<T> = QueryRuleCustomDataViewModel(),
    noinline presenter: QueryRuleCustomDataPresenter<T>? = null,
): QueryRuleCustomDataConnector<T> {
    return QueryRuleCustomDataConnector(viewModel, viewModel.connectSearcher(searcher)).also {
        if (presenter != null) it.subscribe(presenter)
    }
}

/**
 * Create an instance of [QueryRuleCustomDataConnector]
 *
 * @param searcher searcher that handles your searches
 * @param initialItem initial item
 * @param presenter defines the way we want to interact with a model
 */
public inline fun <reified T> QueryRuleCustomDataConnector(
    searcher: SearcherForHits<*>,
    initialItem: T?,
    noinline presenter: QueryRuleCustomDataPresenter<T>? = null,
): QueryRuleCustomDataConnector<T> {
    val viewModel = QueryRuleCustomDataViewModel(initialItem)
    return QueryRuleCustomDataConnector(viewModel, viewModel.connectSearcher(searcher)).also {
        if (presenter != null) it.subscribe(presenter)
    }
}
// endregion
