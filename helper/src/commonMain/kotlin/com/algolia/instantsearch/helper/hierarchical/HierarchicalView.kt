package com.algolia.instantsearch.helper.hierarchical

import com.algolia.instantsearch.core.event.EventView
import com.algolia.instantsearch.core.item.ItemView


public interface HierarchicalView<T> : ItemView<T>, EventView<String>