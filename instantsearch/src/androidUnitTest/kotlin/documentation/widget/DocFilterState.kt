package documentation.widget

import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.add
import com.algolia.instantsearch.filter.state.toFilterGroups
import com.algolia.instantsearch.filter.Filter
import com.algolia.instantsearch.filter.FilterGroupsConverter
import com.algolia.search.model.search.Query
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals

@Ignore
class DocFilterState {

    @Test
    fun addAndRemove() {
        val filterState = FilterState()
        val filterGroupID = FilterGroupID("myGroup", FilterOperator.And)
        val color = "color"
        val red = Filter.Facet(color, "red")
        val green = Filter.Facet(color, "green")
        val blue = Filter.Facet(color, "blue")

        filterState.add(filterGroupID, red, green, blue)
        // "color:red AND color:green AND color:blue"
        filterState.remove(filterGroupID, green)
        // "color:red AND color:blue"
    }

    @Test
    fun notification() {
        val filterState = FilterState()
        val filterGroupID = FilterGroupID("myGroup", FilterOperator.And)
        val color = "color"
        val facets = setOf(
            Filter.Facet(color, "red"),
            Filter.Facet(color, "green"),
            Filter.Facet(color, "blue")
        )
        filterState.notify {
            add(filterGroupID, facets)
        }
        filterState.filters.subscribe { filters ->
            assertEquals(facets, filters.getFacetFilters(filterGroupID))
        }
    }

    @Test
    fun transform() {
        val query = Query()
        val updatedQuery = query.copy(filters = FilterGroupsConverter.SQL(FilterState().toFilterGroups()))
    }
}
