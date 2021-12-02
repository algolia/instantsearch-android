package com.algolia.instantsearch.encode

import com.algolia.instantsearch.Internal

/**
 * Applies GZIP compression to a [ByteArray]
 */
@Internal
public expect fun ByteArray.gzip(): ByteArray
