package com.algolia.instantsearch.helper.searcher


public interface Searcher {

    fun setQuery(text: String?)
    fun search()
    fun cancel()
}