package com.algolia.instantsearch.searcher.internal

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal actual val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO

