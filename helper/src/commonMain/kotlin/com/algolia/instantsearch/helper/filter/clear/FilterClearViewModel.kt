package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.clickable.ClickableViewModel


typealias FilterClearViewModel = ClickableViewModel<Unit>

public fun FilterClearViewModel.click(): Unit = click(Unit)
