package com.algolia.instantsearch.migration2to3

public interface Closeable {

    /**
     * Closes this stream and releases any system resources associated with it.
     * If the stream is already closed then invoking this method has no effect.
     **/
    public fun close()
}
