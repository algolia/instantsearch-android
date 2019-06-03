package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.event.EventViewModelImpl


public typealias FilterClearViewModel = EventViewModelImpl<Unit>

public fun FilterClearViewModel.click(): Unit = trigger(Unit)