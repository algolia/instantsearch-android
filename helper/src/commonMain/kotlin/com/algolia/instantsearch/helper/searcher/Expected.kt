package com.algolia.instantsearch.helper.searcher

import kotlinx.coroutines.CoroutineDispatcher


internal expect val defaultDispatcher : CoroutineDispatcher

internal expect val osVersion: String