package com.algolia.instantsearch.helper.android.internal

import android.util.Base64
import com.algolia.instantsearch.telemetry.Schema
import com.algolia.instantsearch.telemetry.Telemetry

/**
 * Global telemetry controller.
 */
internal val telemetry = Telemetry()

/**
 * Encodes [Schema] byte array representation as base64 string.
 */
internal fun Schema.encodeBase64(): String = Base64.encodeToString(toByteArray(), Base64.URL_SAFE)
