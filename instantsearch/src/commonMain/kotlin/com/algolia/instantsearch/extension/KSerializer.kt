package com.algolia.instantsearch.extension

import kotlinx.serialization.KSerializer

@Suppress("UNCHECKED_CAST")
internal fun <T> KSerializer<*>.cast(): KSerializer<T> = this as KSerializer<T>
