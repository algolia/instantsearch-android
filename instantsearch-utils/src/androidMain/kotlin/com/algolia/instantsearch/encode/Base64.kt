package com.algolia.instantsearch.encode

import android.util.Base64

/**
 * Convert a [ByteArray] to base 64 string.
 */
public actual fun ByteArray.base64(): String {
    return Base64.encodeToString(this, Base64.NO_WRAP)
}
