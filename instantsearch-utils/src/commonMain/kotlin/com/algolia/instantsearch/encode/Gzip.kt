package com.algolia.instantsearch.encode

import com.algolia.instantsearch.InternalInstantSearch

/**
 * Applies GZIP compression to a [ByteArray]
 */
@InternalInstantSearch
public expect fun ByteArray.gzip(): ByteArray
