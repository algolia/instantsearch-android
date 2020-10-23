package com.algolia.instantsearch.ext.customdata

import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.customdata.QueryRuleCustomDataConnector

/**
 * Convenience field to access custom data as SubscriptionValue.
 */
public val <T> QueryRuleCustomDataConnector<T>.model: SubscriptionValue<T?>
    get() = viewModel.item
