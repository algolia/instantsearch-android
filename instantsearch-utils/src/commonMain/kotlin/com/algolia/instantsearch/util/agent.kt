package com.algolia.instantsearch.util

import com.algolia.instantsearch.BuildConfig
import com.algolia.instantsearch.InternalInstantSearch
import com.algolia.instantsearch.platform.osVersion

@InternalInstantSearch
public fun algoliaAgent(libName: String): String = "$libName (${BuildConfig.version}); ${osVersion()}"
