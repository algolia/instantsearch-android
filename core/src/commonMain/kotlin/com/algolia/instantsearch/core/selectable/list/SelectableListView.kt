package com.algolia.instantsearch.core.selectable.list

import com.algolia.instantsearch.core.clickable.ClickableView
import com.algolia.instantsearch.core.item.ItemView


public interface SelectableListView<T>: ClickableView<T>, ItemView<List<SelectableItem<T>>>