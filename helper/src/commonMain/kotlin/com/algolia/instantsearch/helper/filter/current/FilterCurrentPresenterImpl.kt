package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.helper.filter.FilterPresenter
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl


public class FilterCurrentPresenterImpl(
    val comparator: Comparator<Pair<FilterAndID, String>> = Comparator { a, b -> a.second.compareTo(b.second) },
    val presenter: FilterPresenter = FilterPresenterImpl()
) : FilterCurrentPresenter {

    override fun invoke(filterAndIDs: Set<FilterAndID>): List<Pair<FilterAndID, String>> {
        return filterAndIDs.map { it to presenter(it.second) }.sortedWith(comparator)
    }
}