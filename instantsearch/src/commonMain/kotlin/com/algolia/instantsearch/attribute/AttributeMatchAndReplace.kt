package com.algolia.instantsearch.attribute

import com.algolia.instantsearch.filter.Attribute

public class AttributeMatchAndReplace(
    private val match: String,
    private val replacement: String,
) {
    public operator fun invoke(attribute: Attribute): String {
        return if (attribute == match) replacement else attribute
    }
}
