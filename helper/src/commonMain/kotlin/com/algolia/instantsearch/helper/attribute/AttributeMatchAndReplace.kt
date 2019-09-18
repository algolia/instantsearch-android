package com.algolia.instantsearch.helper.attribute

import com.algolia.search.model.Attribute


public class AttributeMatchAndReplace(
    private val match: Attribute,
    private val replacement: String
) : AttributePresenter {

    override fun invoke(attribute: Attribute): String {
        return replace(attribute)
    }

    /**
     * Replaces any [match] by [replacement] in the given [attribute].
     */
    public fun replace(attribute: Attribute): String =
        if (attribute == match) replacement else attribute.raw
}