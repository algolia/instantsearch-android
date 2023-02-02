package com.algolia.instantsearch.encode

import java.io.ByteArrayOutputStream
import java.util.zip.GZIPOutputStream

/**
 * Applies GZIP compression to a [ByteArray]
 */
public actual fun ByteArray.gzip(): ByteArray {
    return ByteArrayOutputStream().use { bos ->
        GZIPOutputStream(bos).buffered().use { gzip -> gzip.write(this) }
        bos.toByteArray()
    }
}
