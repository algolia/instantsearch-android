package com.algolia.instantsearch.helper.customdata

import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.extension.tryOrNull
import com.algolia.search.model.response.ResponseSearch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

/**
 * Component encapsulating the logic applied to the custom model
 *
 * @param item initial item
 * @param type model's type to be deserialized
 */
public class QueryRuleCustomDataViewModel<T>(
    public val serializer: KSerializer<T>,
    item: T? = null,
) {

    public val item: SubscriptionValue<T?> = SubscriptionValue(item)

    public fun extractModel(responseSearch: ResponseSearch) {
        item.value = responseSearch.userDataOrNull?.asSequence()
            ?.mapNotNull { tryOrNull<T> { Json.decodeFromJsonElement(serializer, it) } }
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
