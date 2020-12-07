@file:Suppress("FunctionName")

package com.algolia.instantsearch.helper.filter.range

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.range.internal.FilterRangeConnectionSearcherImpl
import com.algolia.instantsearch.helper.filter.range.internal.mapperOf
import com.algolia.instantsearch.helper.searcher.SearcherIndex
import com.algolia.search.model.Attribute
import kotlin.reflect.KClass

/**
 * Create a connection between a searcher and the filter range components for dynamic behavior.
 * NOTE: It's preferable the use [connectSearcher] over directly using this function.
 *
 * @param viewModel filter range view model to receive the filter stats updates.
 * @param searcher searcher for facets and results extraction operations.
 * @param attribute attribute to dynamically use its facet stats results.
 * @param clazz KClass of a numerical type.
 */
public fun <T> FilterRangeConnectionSearcher(
    viewModel: FilterRangeViewModel<T>,
    searcher: SearcherIndex<*>,
    attribute: Attribute,
    clazz: KClass<T>,
): Connection where T : Number, T : Comparable<T> {
    return FilterRangeConnectionSearcherImpl(viewModel, searcher, attribute, mapperOf(clazz))
}

/**
 * Create a connection between a searcher and the filter range components for dynamic behavior.
 *
 * @param viewModel filter range view model to receive the filter stats updates.
 * @param searcher searcher for facets and results extraction operations.
 * @param attribute attribute to dynamically use its facet stats results.
 * @param mapper explicit mapper to transform facets stats min/max results to the view model's bounds.
 */
public fun <T> FilterRangeConnectionSearcher(
    viewModel: FilterRangeViewModel<T>,
    searcher: SearcherIndex<*>,
    attribute: Attribute,
    mapper: (Number) -> T,
): Connection where T : Number, T : Comparable<T> {
    return FilterRangeConnectionSearcherImpl(viewModel, searcher, attribute, mapper)
}
