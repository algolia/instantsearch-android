package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.helper.filter.FilterPresenter
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl
import com.algolia.search.model.filter.Filter
import kotlin.jvm.JvmOverloads

/**
 * A default Presenter for current filters, represented as a List of FilterID<>String pairs.
 */
public class FilterCurrentPresenterImpl @JvmOverloads constructor(
    val comparator: Comparator<Pair<FilterAndID, String>> = Comparator { a, b -> a.second.compareTo(b.second) },
    val presenter: FilterPresenter = FilterPresenterImpl()
) : FilterCurrentPresenter {

    override fun invoke(filterAndIDs: Map<FilterAndID, Filter>): List<Pair<FilterAndID, String>> {
        return present(filterAndIDs)
    }

    /**
     * Presents the given filters as a String.
     */
    public fun present(filterAndIDs: Map<FilterAndID, Filter>) =
        filterAndIDs.map { (key, value) -> key to presenter(value) }.sortedWith(comparator)
}