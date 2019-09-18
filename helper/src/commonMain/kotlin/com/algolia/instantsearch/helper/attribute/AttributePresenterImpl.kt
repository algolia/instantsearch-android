package com.algolia.instantsearch.helper.attribute

import com.algolia.search.model.Attribute


public class AttributePresenterImpl(
    private val transform: (Attribute) -> String = { attribute -> attribute.raw }
) : AttributePresenter {

    override fun invoke(attribute: Attribute): String {
        return present(attribute)
    }

    /**
     * Presents the given [attribute] as a [String].
     */
    public fun present(attribute: Attribute) = transform(attribute)
}