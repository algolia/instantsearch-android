package filter

import com.algolia.search.model.filter.Filter


public class FilterState(
    private val filters: MutableFilters = MutableFiltersImpl()
) : MutableFilters by filters {

    public constructor(
        facets: MutableMap<FilterGroupID, Set<Filter.Facet>> = mutableMapOf(),
        tags: MutableMap<FilterGroupID, Set<Filter.Tag>> = mutableMapOf(),
        numerics: MutableMap<FilterGroupID, Set<Filter.Numeric>> = mutableMapOf()
    ) : this(MutableFiltersImpl(facets, tags, numerics))

    public val onStateChanged: MutableList<(Filters) -> Unit> = mutableListOf()

    public fun notify(block: MutableFilters.() -> Unit) {
        block(filters)
        onStateChanged.forEach { it(filters) }
    }
}