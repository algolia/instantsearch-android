package com.algolia.instantsearch.helper.searcher

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


public class SearcherScope : CoroutineScope {

    override val coroutineContext = SupervisorJob()
}