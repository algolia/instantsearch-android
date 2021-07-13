package com.algolia.instantsearch.helper.filter.facet.dynamic

import com.algolia.search.model.Attribute

/**
 * Mapping between a facet attribute and a set of selected facet values.
 */
public typealias SelectionsPerAttribute = Map<Attribute, Set<String>>
