package com.algolia.instantsearch.insights.internal.data.local.model

internal typealias FilterFacetDO = Map<String, Any?>

internal enum class FacetKey(val raw: String) {
    Attribute("attribute"),
    IsNegated("isNegated"),
    Value("value"),
    ValueType("valueType"),
    Score("score")
}

internal enum class ValueType(val raw: kotlin.String) {
    String("string"),
    Boolean("boolean"),
    Number("number");

    companion object {
        fun of(value: kotlin.String): ValueType {
            return values().first { it.raw == value }
        }
    }
}
