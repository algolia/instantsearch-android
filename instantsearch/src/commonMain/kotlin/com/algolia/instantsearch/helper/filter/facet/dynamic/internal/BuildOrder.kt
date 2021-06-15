package com.algolia.instantsearch.helper.filter.facet.dynamic.internal

import com.algolia.search.model.Attribute
import com.algolia.search.model.rule.AttributedFacets
import com.algolia.search.model.rule.FacetOrdering
import com.algolia.search.model.rule.OrderingRule
import com.algolia.search.model.rule.SortRule
import com.algolia.search.model.search.Facet

internal class BuildOrder(
    val facetOrdering: FacetOrdering,
    val facets: Map<Attribute, List<Facet>>
) {

    operator fun invoke(): List<AttributedFacets> {
        return order(
            input = facets.toList(),
            rule = facetOrdering.facets,
            valuePath = { facet: Pair<Attribute, List<Facet>> -> facet.first.raw },
            countPath = { facet: Pair<Attribute, List<Facet>> -> facet.second.count() }
        ).map { (attribute, facets) ->
            val rule = facetOrdering.values[attribute] ?: OrderingRule()
            val orderedFacetValues = order(
                input = facets,
                rule = rule,
                valuePath = Facet::value,
                countPath = Facet::count
            )
            AttributedFacets(attribute, orderedFacetValues)
        }
    }

    private fun <T> order(
        input: List<T>,
        rule: OrderingRule,
        valuePath: (T) -> String,
        countPath: (T) -> Int
    ): List<T> {
        val order = rule.order ?: listOf(WILDCARD)
        val hide = rule.hide ?: emptyList()
        val filterPredicate = filterPredicate(valuePath, order, hide)
        val predicate = predicate(valuePath, countPath, order, rule.sortBy)
        return input
            .filter { filterPredicate(it) }
            .sortedWith { a, b -> predicate(a, b) }
    }

    private fun <T> filterPredicate(
        valuePath: (T) -> String,
        order: List<String>,
        hide: List<String>
    ): (T) -> Boolean {
        return { obj ->
            val value = valuePath(obj)
            !hide.contains(value) && (order.contains(WILDCARD) || order.contains(value))
        }
    }

    private fun <T> predicate(
        valuePath: (T) -> String,
        countPath: (T) -> Int,
        order: List<String>,
        sortBy: SortRule?
    ): (T, T) -> Int {
        return { left, right ->
            val leftIndex = order.indexOfFirstOrNull { it == valuePath(left) }
            val rightIndex = order.indexOfFirstOrNull { it == valuePath(right) }
            when {
                leftIndex != null && rightIndex != null -> leftIndex.compareTo(rightIndex)
                leftIndex != null && rightIndex == null -> -1
                leftIndex == null && rightIndex != null -> 1
                else -> {
                    when (sortBy ?: SortRule.Alpha) {
                        SortRule.Alpha -> valuePath(left).compareTo(valuePath(right))
                        SortRule.Count -> countPath(left).compareToReverse(countPath(right))
                    }
                }
            }
        }
    }

    private fun <T> List<T>.indexOfFirstOrNull(predicate: (T) -> Boolean): Int? {
        val value = indexOfFirst(predicate)
        return if (value >= 0) value else null
    }

    private fun <T> Comparable<T>.compareToReverse(other: T): Int = compareTo(other) * -1

    companion object {
        private const val WILDCARD = "*"
    }
}
