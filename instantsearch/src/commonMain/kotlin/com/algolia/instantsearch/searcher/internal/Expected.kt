package com.algolia.instantsearch.searcher.internal

import kotlinx.coroutines.CoroutineDispatcher

internal expect val defaultDispatcher: CoroutineDispatcher

internal expect val osVersion: String
