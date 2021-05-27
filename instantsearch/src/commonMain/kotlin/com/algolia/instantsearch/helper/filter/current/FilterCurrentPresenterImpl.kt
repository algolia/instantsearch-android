package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.helper.filter.FilterPresenter
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl
import com.algolia.search.model.filter.Filter

public class FilterCurrentPresenterImpl(
    public val comparator: Comparator<Pair<FilterAndID, String>> = Comparator { a, b -> a.second.compareTo(b.second) },
    public val presenter: FilterPresenter = FilterPresenterImpl(),
) : FilterCurrentPresenter {

    override fun invoke(filterAndIDs: Map<FilterAndID, Filter>): List<Pair<FilterAndID, String>> {
        return filterAndIDs.map { (key, value) -> key to presenter(value) }.sortedWith(comparator)
    }
}
