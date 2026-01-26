package com.algolia.instantsearch.attribute

public class AttributeMatchAndReplace(
    private val match: String,
    private val replacement: String,
) {
    public operator fun invoke(attribute: String): String {
        return if (attribute == match) replacement else attribute
    }
}
