package com.algolia.instantsearch.hierarchical

import com.algolia.search.model.Attribute

/**
 * Hierarchical path in a tree.
 *
 * Example: [("lvl0", "A"), ("lvl1", "A > B"), ("lvl2", "A > B > C")]
 */
public typealias HierarchicalPath = List<Pair<Attribute, String>>
