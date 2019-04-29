package filter

import com.algolia.search.model.filter.Filter


public class FilterState(
    private val filters: MutableFilters = MutableFiltersImpl()
) : MutableFilters by filters {

    public constructor(
        facetGroups: MutableMap<FilterGroupID, Set<Filter.Facet>> = mutableMapOf(),
        tagGroups: MutableMap<FilterGroupID, Set<Filter.Tag>> = mutableMapOf(),
        numericGroups: MutableMap<FilterGroupID, Set<Filter.Numeric>> = mutableMapOf()
    ) : this(MutableFiltersImpl(facetGroups, tagGroups, numericGroups))

    public val onChange: MutableList<(Filters) -> Unit> = mutableListOf()

    public fun notify(block: MutableFilters.() -> Unit) {
        block(filters)
        notifyChange()
    }

    public fun notifyChange() {
        onChange.forEach { it(filters) }
    }
}