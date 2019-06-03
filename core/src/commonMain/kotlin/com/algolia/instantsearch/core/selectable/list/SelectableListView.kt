package com.algolia.instantsearch.core.selectable.list

import com.algolia.instantsearch.core.event.EventView
import com.algolia.instantsearch.core.item.ItemView


public interface SelectableListView<T>: EventView<T>, ItemView<List<SelectableItem<T>>>