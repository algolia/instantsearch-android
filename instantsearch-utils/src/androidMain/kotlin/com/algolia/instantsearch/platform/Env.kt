package com.algolia.instantsearch.platform

import android.os.Build

internal actual fun osVersion(): String = "Android (${Build.VERSION.SDK_INT})"
