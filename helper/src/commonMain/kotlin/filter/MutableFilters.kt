package filter

import com.algolia.search.model.filter.Filter
import search.GroupID


public interface MutableFilters: Filters {

    fun <T : Filter> add(groupID: GroupID, vararg filters: T)

    fun <T : Filter> remove(groupID: GroupID, vararg filters: T)

    fun <T : Filter> toggle(groupID: GroupID, filter: T)

    fun clear(groupID: GroupID? = null)
}