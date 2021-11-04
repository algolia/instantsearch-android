package com.algolia.instantsearch.helper.android.internal

import android.util.Base64
import com.algolia.instantsearch.telemetry.Schema
import com.algolia.instantsearch.telemetry.Telemetry

/**
 * Global telemetry controller.
 */
internal val telemetry = Telemetry()

/**
 * Encode [Schema] to base64 string.
 */
internal fun Schema.encodeBase64(): String = Base64.encodeToString(toByteArray(), Base64.DEFAULT)
