package com.algolia.instantsearch.helper.hierarchical

import com.algolia.instantsearch.core.tree.TreePresenter
import com.algolia.search.model.search.Facet

public typealias HierarchicalPresenter<T> = TreePresenter<Facet, T>
