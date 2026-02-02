package com.algolia.instantsearch.attribute

public class DefaultAttributePresenter(
    private val transform: (String) -> String = { attribute -> attribute },
) : AttributePresenter {

    override fun invoke(attribute: String): String {
        return transform(attribute)
    }
}

@Deprecated(message = "use DefaultAttributePresenter instead", replaceWith = ReplaceWith("DefaultAttributePresenter"))
public typealias AttributePresenterImpl = DefaultAttributePresenter
