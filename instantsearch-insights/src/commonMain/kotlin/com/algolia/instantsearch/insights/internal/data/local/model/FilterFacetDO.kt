package com.algolia.instantsearch.insights.internal.data.local.model

import com.algolia.search.model.Attribute
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive

@Serializable
internal data class FilterFacetDO(
    @SerialName("attribute") val attribute: Attribute,
    @SerialName("isNegated") val isNegated: Boolean,
    @SerialName("value") val value: JsonPrimitive,
    @SerialName("valueType") val valueType: ValueType,
    @SerialName("score") val score: Int?
) {

    @Serializable
    internal enum class ValueType {
        @SerialName("string")
        String,

        @SerialName("boolean")
        Boolean,

        @SerialName("number")
        Number;
    }
}
