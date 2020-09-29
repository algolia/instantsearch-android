package com.algolia.instantsearch.insights.internal.database.mapper

import com.algolia.instantsearch.insights.internal.database.model.FacetKey
import com.algolia.instantsearch.insights.internal.database.model.FilterFacetDO
import com.algolia.instantsearch.insights.internal.database.model.ValueType
import com.algolia.search.helper.toAttribute
import com.algolia.search.model.filter.Filter

internal object FilterFacetMapper : Mapper<Filter.Facet, FilterFacetDO> {

    @OptIn(ExperimentalStdlibApi::class)
    override fun map(input: Filter.Facet): FilterFacetDO {
        return buildMap {
            put(FacetKey.Attribute.raw, input.attribute.raw)
            put(FacetKey.IsNegated.raw, input.isNegated)
            put(FacetKey.Value.raw, input.value.getRaw())
            put(FacetKey.ValueType.raw, input.value.getType().raw)
            input.score?.let { put(FacetKey.Score.raw, it) }
        }
    }

    private fun Filter.Facet.Value.getRaw(): Any {
        return when (this) {
            is Filter.Facet.Value.String -> raw
            is Filter.Facet.Value.Boolean -> raw
            is Filter.Facet.Value.Number -> raw
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
        val attribute = input.getValue(FacetKey.Attribute.raw).toString().toAttribute()
        val isNegated = input.getValue(FacetKey.IsNegated.raw) as Boolean
        val valueType = ValueType.of((input.getValue(FacetKey.ValueType.raw) as String))
        val value = input.getValue(FacetKey.Value.raw)
        val score = input[FacetKey.Score.raw] as Int?
        return when (valueType) {
            ValueType.String -> Filter.Facet(
                attribute = attribute,
                value = value as String,
                isNegated = isNegated,
                score = score
            )
            ValueType.Boolean -> Filter.Facet(
                attribute = attribute,
                value = value as Boolean,
                isNegated = isNegated,
                score = score
            )
            ValueType.Number -> Filter.Facet(
                attribute = attribute,
                value = value as Number,
                isNegated = isNegated,
                score = score
            )
        }
    }
}
