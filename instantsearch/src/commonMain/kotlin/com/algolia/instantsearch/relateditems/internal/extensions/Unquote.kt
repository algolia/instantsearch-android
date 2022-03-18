package com.algolia.instantsearch.relateditems.internal.extensions

/**
 * TODO: Replace and remove after https://github.com/algolia/algoliasearch-client-kotlin/issues/188
 */
internal fun List<List<String>>.unquote(): List<List<String>> {
    return map { innerList ->
        innerList.map { it.replace("\"", "") }
    }
}
