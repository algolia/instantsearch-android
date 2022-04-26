package com.algolia.instantsearch.core.number

public object DefaultNumberPresenter : NumberPresenter<Number> {

    override fun invoke(number: Number?): String {
        return number?.toString() ?: "-"
    }
}

@Deprecated(message = "use DefaultNumberPresenter instead", replaceWith = ReplaceWith("DefaultNumberPresenter"))
public typealias NumberPresenterImpl = DefaultNumberPresenter
