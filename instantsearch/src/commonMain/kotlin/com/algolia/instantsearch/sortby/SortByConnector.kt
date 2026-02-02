@file:Suppress("FunctionName")

package com.algolia.instantsearch.sortby

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.searcher.IndexNameHolder
import com.algolia.instantsearch.sortby.internal.DefaultSortByConnector

/**
 * SortBy displays a list of indices, allowing a user to change the way hits are sorted (using replica indices).
 * Another common use case is to let the user switch between different indices to show different results.
 * For this to work, you must define all indices that you pass to SortBy as replicas of the main index.
 *
 * [Documentation](https://www.algolia.com/doc/api-reference/widgets/sort-by/android/)
 */
public interface SortByConnector<S> : Connection where S : Searcher<*>, S : IndexNameHolder {

    /**
     * Searcher that handles your searches.
     */
    public val searcher: S

    /**
     * Logic applied to the index sorting/switching.
     */
    public val viewModel: SortByViewModel
}

/**
 * Create an instance of [SortByConnector].
 *
 * @param searcher the Searcher that handles your searches
 * @param viewModel the logic applied to the index sorting/switching
 */
public fun <S> SortByConnector(
    searcher: S,
    viewModel: SortByViewModel = SortByViewModel(),
): SortByConnector<S> where S : Searcher<*>, S : IndexNameHolder = DefaultSortByConnector(searcher, viewModel)

/**
 * Create an instance of [SortByConnector].
 *
 * @param indexes the list of indices to search in
 * @param searcher the Searcher that handles your searches
 * @param selected the index to select. By default, none is selected.
 */
public fun <S> SortByConnector(
    searcher: S,
    indexes: Map<Int, String>,
    selected: Int? = null,
): SortByConnector<S> where S : Searcher<*>, S : IndexNameHolder = DefaultSortByConnector(
    searcher = searcher,
    viewModel = SortByViewModel(indexes, selected)
)
