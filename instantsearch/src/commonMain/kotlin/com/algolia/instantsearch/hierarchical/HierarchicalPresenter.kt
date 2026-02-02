package com.algolia.instantsearch.hierarchical

import com.algolia.client.model.search.FacetHits
import com.algolia.instantsearch.core.tree.TreePresenter

public typealias HierarchicalPresenter<T> = TreePresenter<FacetHits, T>
