package com.algolia.instantsearch.core.map

import com.algolia.instantsearch.core.event.EventView
import com.algolia.instantsearch.core.item.ItemView


public interface MapView<K, V> : EventView<K>, ItemView<Map<K, V>>