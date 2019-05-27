package com.algolia.instantsearch.helper.attribute

import com.algolia.search.model.Attribute


public class AttributePresenterImpl(
    val transform: (Attribute) -> String = { attribute -> attribute.raw }
) :
    AttributePresenter {

    override fun invoke(attribute: Attribute): String {
        return transform(attribute)
    }
}