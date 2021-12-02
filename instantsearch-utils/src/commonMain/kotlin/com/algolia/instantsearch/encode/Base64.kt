package com.algolia.instantsearch.encode

import com.algolia.instantsearch.Internal

/**
 * Convert a [ByteArray] to base 64 string.
 */
@Internal
public expect fun ByteArray.base64(): String
