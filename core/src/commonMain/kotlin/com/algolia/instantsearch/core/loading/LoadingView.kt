package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.event.EventView
import com.algolia.instantsearch.core.item.ItemView


public interface LoadingView : ItemView<Boolean>, EventView<Unit>