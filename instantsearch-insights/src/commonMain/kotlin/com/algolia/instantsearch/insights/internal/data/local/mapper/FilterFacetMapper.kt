@file:OptIn(InternalSerializationApi::class)

package com.algolia.instantsearch.insights.internal.data.local.mapper

import com.algolia.instantsearch.insights.internal.data.local.model.FilterFacetDO
import com.algolia.instantsearch.insights.internal.data.local.model.FilterFacetDO.ValueType
import com.algolia.instantsearch.migration2to3.Filter
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonPrimitive

internal object FilterFacetMapper : Mapper<Filter.Facet, FilterFacetDO> {

    override fun map(input: Filter.Facet): FilterFacetDO {
        return FilterFacetDO(
            attribute = input.attribute,
            isNegated = input.isNegated,
            value = input.value.asJsonPrimitive(),
            valueType = input.value.getType(),
            score = input.score
        )
    }

    private fun Filter.Facet.Value.asJsonPrimitive(): JsonPrimitive {
        return when (this) {
            is Filter.Facet.Value.String -> JsonPrimitive(raw)
            is Filter.Facet.Value.Boolean -> JsonPrimitive(raw)
            is Filter.Facet.Value.Number -> JsonPrimitive(raw)
        }
    }

    private fun Filter.Facet.Value.getType(): ValueType {
        return when (this) {
            is Filter.Facet.Value.String -> ValueType.String
            is Filter.Facet.Value.Boolean -> ValueType.Boolean
            is Filter.Facet.Value.Number -> ValueType.Number
        }
    }

    override fun unmap(input: FilterFacetDO): Filter.Facet {
        val primitiveValue = input.value.jsonPrimitive
        return input.run {
            when (input.valueType) {
                ValueType.String -> Filter.Facet(
                    attribute = attribute,
                    value = primitiveValue.content,
                    isNegated = isNegated,
                    score = score
                )
                ValueType.Boolean -> Filter.Facet(
                    attribute = attribute,
                    value = primitiveValue.boolean,
                    isNegated = isNegated,
                    score = score
                )
                ValueType.Number -> Filter.Facet(
                    attribute = attribute,
                    value = primitiveValue.double,
                    isNegated = isNegated,
                    score = score
                )
            }
        }
    }
}
