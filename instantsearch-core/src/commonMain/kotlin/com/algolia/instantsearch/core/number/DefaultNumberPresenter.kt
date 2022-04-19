package com.algolia.instantsearch.core.number

public object DefaultNumberPresenter : NumberPresenter<Number> {

    override fun invoke(number: Number?): String {
        return number?.toString() ?: "-"
    }
}
