package com.algolia.instantsearch.searcher

import com.algolia.instantsearch.filter.FilterGroup

/**
 * Component holding filter groups set.
 */
public interface FilterGroupsHolder {

    /**
     * Contains a [Set] of [FilterGroup], used for disjunctive and hierarchical faceting.
     */
    public var filterGroups: Set<FilterGroup<*>>
}
