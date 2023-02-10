package com.algolia.instantsearch.core

import com.algolia.instantsearch.BuildConfig

public object InstantSearch {

    public const val version: String = BuildConfig.version

    public const val userAgent: String = "InstantSearchAndroid ($version)"
}
