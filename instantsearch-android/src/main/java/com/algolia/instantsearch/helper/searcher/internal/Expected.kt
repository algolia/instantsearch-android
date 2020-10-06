package com.algolia.instantsearch.helper.searcher.internal

import android.os.Build
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal val defaultDispatcher: CoroutineDispatcher = Dispatchers.Main

internal val osVersion = "Android (${Build.VERSION.SDK_INT})"
