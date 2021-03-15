package com.algolia.instantsearch.insights.internal.extension

internal fun <K, V> MutableMap<K, V>.put(entry: Pair<K, V>) {
    put(entry.first, entry.second)
}
