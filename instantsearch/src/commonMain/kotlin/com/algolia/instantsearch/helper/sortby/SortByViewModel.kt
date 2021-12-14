package com.algolia.instantsearch.helper.sortby

import com.algolia.instantsearch.core.selectable.map.SelectableMapViewModel
import com.algolia.search.client.Index

@Deprecated("use SortByViewModel with IndexName instead", ReplaceWith("com.algolia.instantsearch.helper.sortby.searcher.SortByViewModel"))
public typealias SortByViewModel = SelectableMapViewModel<Int, Index>
