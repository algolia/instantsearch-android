package com.algolia.instantsearch.hierarchical

import com.algolia.instantsearch.core.tree.TreePresenter
import com.algolia.instantsearch.migration2to3.Facet

public typealias HierarchicalPresenter<T> = TreePresenter<Facet, T>
