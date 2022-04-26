package com.algolia.instantsearch.attribute

import com.algolia.search.model.Attribute

public class DefaultAttributePresenter(
    private val transform: (Attribute) -> String = { attribute -> attribute.raw },
) : AttributePresenter {

    override fun invoke(attribute: Attribute): String {
        return transform(attribute)
    }
}

@Deprecated(message = "use DefaultAttributePresenter instead", replaceWith = ReplaceWith("DefaultAttributePresenter"))
public typealias AttributePresenterImpl = DefaultAttributePresenter
