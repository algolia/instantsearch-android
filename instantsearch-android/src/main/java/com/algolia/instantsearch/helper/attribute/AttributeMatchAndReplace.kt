package com.algolia.instantsearch.helper.attribute

import com.algolia.search.model.Attribute

public class AttributeMatchAndReplace(
    private val match: Attribute,
    private val replacement: String,
) : AttributePresenter {

    override fun invoke(attribute: Attribute): String {
        return if (attribute == match) replacement else attribute.raw
    }
}
