package com.algolia.instantsearch.relateditems.internal.extensions

internal fun List<List<String>>.unquote(): List<List<String>> {
    return map { innerList ->
        innerList.map { it.replace("\"", "") }
    }
}
