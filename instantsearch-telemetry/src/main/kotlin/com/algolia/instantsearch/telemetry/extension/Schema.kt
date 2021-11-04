package com.algolia.instantsearch.telemetry.extension

import com.algolia.instantsearch.telemetry.Schema

/**
 * Get [Schema] as Json string.
 */
public fun Schema.asJsonString(): String = "JsonFormat.printer().print(this)"
