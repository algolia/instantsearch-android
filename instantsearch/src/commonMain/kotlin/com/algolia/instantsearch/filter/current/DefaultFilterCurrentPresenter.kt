package com.algolia.instantsearch.filter.current

import com.algolia.instantsearch.filter.DefaultFilterPresenter
import com.algolia.instantsearch.filter.FilterPresenter
import com.algolia.instantsearch.filter.Filter

public class DefaultFilterCurrentPresenter(
    public val comparator: Comparator<Pair<FilterAndID, String>> = Comparator { a, b -> a.second.compareTo(b.second) },
    public val presenter: FilterPresenter = DefaultFilterPresenter(),
) : FilterCurrentPresenter {

    override fun invoke(filterAndIDs: Map<FilterAndID, Filter>): List<Pair<FilterAndID, String>> {
        return filterAndIDs.map { (key, value) -> key to presenter(value) }.sortedWith(comparator)
    }
}

@Deprecated(
    message = "use DefaultFilterCurrentPresenter instead",
    replaceWith = ReplaceWith("DefaultFilterCurrentPresenter")
)
public typealias FilterCurrentPresenterImpl = DefaultFilterCurrentPresenter
