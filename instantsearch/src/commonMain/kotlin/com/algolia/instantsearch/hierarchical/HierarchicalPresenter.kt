package com.algolia.instantsearch.hierarchical

import com.algolia.instantsearch.core.tree.TreePresenter
import com.algolia.instantsearch.filter.Facet

public typealias HierarchicalPresenter<T> = TreePresenter<Facet, T>
