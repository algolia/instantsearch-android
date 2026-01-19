package com.algolia.instantsearch.core

/**
 * Interface for objects that have an objectID property.
 * This is used for tracking hits in insights events.
 */
public interface Indexable {
    /**
     * Unique record identifier.
     */
    public val objectID: String
}

