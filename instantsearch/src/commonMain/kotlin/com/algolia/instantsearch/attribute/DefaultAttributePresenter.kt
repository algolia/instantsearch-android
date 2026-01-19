package com.algolia.instantsearch.attribute

import com.algolia.instantsearch.filter.Attribute

public class DefaultAttributePresenter(
    private val transform: (Attribute) -> String = { attribute -> attribute },
) : AttributePresenter {

    override fun invoke(attribute: Attribute): String {
        return transform(attribute)
    }
}

@Deprecated(message = "use DefaultAttributePresenter instead", replaceWith = ReplaceWith("DefaultAttributePresenter"))
public typealias AttributePresenterImpl = DefaultAttributePresenter
