package com.algolia.instantsearch.helper.filter.state

import com.algolia.search.model.filter.Filter


public interface MutableFilters: Filters {

    fun <T : Filter> add(groupID: FilterGroupID, vararg filters: T)

    fun <T : Filter> remove(groupID: FilterGroupID, vararg filters: T)

    fun <T : Filter> toggle(groupID: FilterGroupID, filter: T)

    fun clear(groupID: FilterGroupID? = null)
}