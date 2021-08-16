package com.algolia.instantsearch.helper.searcher.internal

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal val defaultDispatcher: CoroutineDispatcher = Dispatchers.Main

internal expect val osVersion: String
