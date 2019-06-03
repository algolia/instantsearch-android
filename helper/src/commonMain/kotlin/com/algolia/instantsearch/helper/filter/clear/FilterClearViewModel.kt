package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.event.EventViewModel


public typealias FilterClearViewModel = EventViewModel<Unit>

public fun FilterClearViewModel.click(): Unit = trigger(Unit)