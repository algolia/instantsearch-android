package com.algolia.instantsearch.customdata

import com.algolia.client.model.search.SearchResponse
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.extension.traceQueryRuleCustomData
import com.algolia.instantsearch.extension.tryOrNull
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

/**
 * Component encapsulating the logic applied to the custom model
 *
 * @param deserializer deserializes the model into a value of type T
 * @param initialItem initial item
 */
public class QueryRuleCustomDataViewModel<T>(
    private val deserializer: DeserializationStrategy<T>,
    initialItem: T? = null,
) {

    public val item: SubscriptionValue<T?> = SubscriptionValue(initialItem)

    init {
        traceQueryRuleCustomData(initialItem != null)
    }

    public fun extractModel(responseSearch: SearchResponse) {
        item.value = responseSearch.userData?.asSequence()
            ?.mapNotNull { tryOrNull { Json.decodeFromJsonElement(deserializer, it) } }
            ?.firstOrNull()
    }
}

/**
 * Component encapsulating the logic applied to the custom model
 *
 * @param initialItem initial item
 */
@Suppress("FunctionName")
public inline fun <reified T> QueryRuleCustomDataViewModel(initialItem: T? = null): QueryRuleCustomDataViewModel<T> {
    return QueryRuleCustomDataViewModel(serializer(), initialItem)
}
