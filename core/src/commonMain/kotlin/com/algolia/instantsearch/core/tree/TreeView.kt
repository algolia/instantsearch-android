package com.algolia.instantsearch.core.tree

import com.algolia.instantsearch.core.event.EventView
import com.algolia.instantsearch.core.item.ItemView


public interface TreeView<K, V> : ItemView<V>, EventView<K>