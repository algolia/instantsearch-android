package com.algolia.instantsearch.filter.facet.dynamic

import com.algolia.instantsearch.filter.Attribute

/**
 * Mapping between a facet attribute and a set of selected facet values.
 */
public typealias SelectionsPerAttribute = Map<Attribute, Set<String>>
