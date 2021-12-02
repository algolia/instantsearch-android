@file:Suppress("NewApi")

package com.algolia.instantsearch.encode

import java.util.Base64

/**
 * Convert a [ByteArray] to base 64 string.
 */
public actual fun ByteArray.base64(): String {
    return Base64.getEncoder().encodeToString(this)
}
