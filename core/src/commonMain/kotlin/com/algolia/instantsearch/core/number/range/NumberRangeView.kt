package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.event.EventView
import com.algolia.instantsearch.core.item.ItemView
import com.algolia.instantsearch.core.number.Range


public interface NumberRangeView<T : Number> : ItemView<Range<T>?>, EventView<Range<T>>