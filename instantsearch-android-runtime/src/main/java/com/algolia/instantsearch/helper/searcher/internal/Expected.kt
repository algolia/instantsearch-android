package com.algolia.instantsearch.helper.searcher.internal

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal val defaultDispatcher: CoroutineDispatcher = Dispatchers.Main

internal val osVersion = "Android " //TODO: (${Build.VERSION.SDK_INT})"

