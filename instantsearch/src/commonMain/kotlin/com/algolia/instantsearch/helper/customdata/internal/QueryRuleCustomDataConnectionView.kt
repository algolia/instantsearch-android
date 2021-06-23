package com.algolia.instantsearch.helper.customdata.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.customdata.QueryRuleCustomDataPresenter
import com.algolia.instantsearch.helper.customdata.QueryRuleCustomDataView
import com.algolia.instantsearch.helper.customdata.QueryRuleCustomDataViewModel

/**
 * Connection between a rule custom data logic and a view.
 *
 * @param viewModel logic applied to the custom model
 * @param view the view that will render the custom data
 * @param presenter defines the way we want to interact with custom data
 */
internal class QueryRuleCustomDataConnectionView<T, R>(
    private val viewModel: QueryRuleCustomDataViewModel<T>,
    private val view: QueryRuleCustomDataView<R>,
    private val presenter: QueryRuleCustomDataPresenter<T, R>,
) : ConnectionImpl() {

    private val itemSubscription: Callback<T?> = {
        view.setModel(presenter(it))
    }

    override fun connect() {
        super.connect()
        viewModel.item.subscribePast(itemSubscription)
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.item.unsubscribe(itemSubscription)
    }
}
