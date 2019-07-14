package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.event.EventView
import com.algolia.instantsearch.core.item.ItemView


public interface FilterCurrentView : ItemView<List<Pair<FilterAndID, String>>>, EventView<FilterAndID>