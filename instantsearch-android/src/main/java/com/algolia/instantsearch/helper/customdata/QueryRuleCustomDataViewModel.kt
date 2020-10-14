package com.algolia.instantsearch.helper.customdata

import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.extension.tryOrNull
import com.algolia.search.model.response.ResponseSearch
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

    public fun extractModel(responseSearch: ResponseSearch) {
        item.value = responseSearch.userDataOrNull?.asSequence()
            ?.mapNotNull { tryOrNull<T> { Json.decodeFromJsonElement(deserializer, it) } }
            ?.firstOrNull()
    }
}

/**
 * Component encapsulating the logic applied to the custom model
 *
 * @param item initial item
 */
@Suppress("FunctionName")
public inline fun <reified T> QueryRuleCustomDataViewModel(item: T? = null): QueryRuleCustomDataViewModel<T> {
    return QueryRuleCustomDataViewModel(serializer(), item)
}
