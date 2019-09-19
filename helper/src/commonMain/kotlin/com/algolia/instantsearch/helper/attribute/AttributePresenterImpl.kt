package com.algolia.instantsearch.helper.attribute

import com.algolia.search.model.Attribute

/**
 * A default attribute presenter, using their canonical String representation.
 */
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