package com.algolia.instantsearch.helper.searcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


internal actual val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default