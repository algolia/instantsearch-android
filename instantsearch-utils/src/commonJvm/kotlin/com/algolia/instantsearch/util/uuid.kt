package com.algolia.instantsearch.util

import java.util.*

public actual fun randomUuid(): String {
    return UUID.randomUUID().toString()
}
