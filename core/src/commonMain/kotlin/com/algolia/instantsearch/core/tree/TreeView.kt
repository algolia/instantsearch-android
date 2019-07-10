package com.algolia.instantsearch.core.tree

import com.algolia.instantsearch.core.event.EventView
import com.algolia.instantsearch.core.item.ItemView


public interface TreeView<T> : ItemView<T>, EventView<String>