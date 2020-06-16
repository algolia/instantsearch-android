package com.algolia.instantsearch.insights.internal.extension

import org.json.JSONArray

internal fun JSONArray.toList(): List<Any> {
    return mutableListOf<Any>().also {
        for (i in 0 until this.length()) {
            it.add(this[i])
        }
    }
}
