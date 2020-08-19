package com.algolia.instantsearch.helper.searcher

import android.os.Build
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


internal actual val defaultDispatcher: CoroutineDispatcher = Dispatchers.Main

internal actual val osVersion = "Android (${Build.VERSION.SDK_INT})"