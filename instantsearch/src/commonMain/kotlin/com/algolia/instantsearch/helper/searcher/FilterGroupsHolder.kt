package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.search.model.filter.FilterGroup

/**
 * Component holding filter groups set.
 */
@ExperimentalInstantSearch
public interface FilterGroupsHolder {

    /**
     * Contains a [Set] of [FilterGroup], used for disjunctive and hierarchical faceting.
     */
    public var filterGroups: Set<FilterGroup<*>>
}
