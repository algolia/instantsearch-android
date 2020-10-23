package com.algolia.instantsearch.ext.searchbox

import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector

/**
 * Convenience field to access custom data as SubscriptionValue.
 */
public val <T> SearchBoxConnector<T>.submits: SubscriptionEvent<String?>
    get() = viewModel.eventSubmit
