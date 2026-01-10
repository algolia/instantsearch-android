package com.algolia.instantsearch.filter.state

import com.algolia.instantsearch.migration2to3.Attribute


/**
 * A descriptor of a filter group.
 */
public typealias FilterGroupDescriptor = Pair<Attribute, FilterOperator>
