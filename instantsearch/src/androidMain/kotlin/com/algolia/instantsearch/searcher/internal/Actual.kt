package com.algolia.instantsearch.searcher.internal

import android.os.Build
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal actual val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO

internal actual val osVersion = "Android (${Build.VERSION.SDK_INT})"
