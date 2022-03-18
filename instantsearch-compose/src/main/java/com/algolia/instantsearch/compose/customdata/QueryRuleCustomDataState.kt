@file:Suppress("FunctionName")

package com.algolia.instantsearch.compose.customdata

import com.algolia.instantsearch.compose.customdata.internal.QueryRuleCustomDataStateImpl
import com.algolia.instantsearch.customdata.QueryRuleCustomDataPresenter

/**
 * [QueryRuleCustomDataPresenter] for compose.
 */
public interface QueryRuleCustomDataState<T> : QueryRuleCustomDataPresenter<T> {

    /**
     * Custom data item value.
     */
    public val item: T?
}

/**
 * Creates an instance of [QueryRuleCustomDataState].
 *
 * @param initialItem initial item value
 */
public fun <T> QueryRuleCustomDataState(initialItem: T? = null): QueryRuleCustomDataState<T> {
    return QueryRuleCustomDataStateImpl(initialItem)
}
