package filter

import com.algolia.search.model.filter.Filter
import search.GroupID


public class FilterState(
    private val filters: MutableFilters = MutableFiltersImpl()
) : MutableFilters by filters {

    public constructor(
        facets: MutableMap<GroupID, Set<Filter.Facet>> = mutableMapOf(),
        tags: MutableMap<GroupID, Set<Filter.Tag>> = mutableMapOf(),
        numerics: MutableMap<GroupID, Set<Filter.Numeric>> = mutableMapOf()
    ) : this(MutableFiltersImpl(facets, tags, numerics))

    public val listeners: MutableList<(Filters) -> Unit> = mutableListOf()

    public fun notify(block: MutableFilters.() -> Unit) {
        block(filters)
        listeners.forEach { it(filters) }
    }

    fun notifyListeners() {
        listeners.forEach { it(filters) }
    }
}