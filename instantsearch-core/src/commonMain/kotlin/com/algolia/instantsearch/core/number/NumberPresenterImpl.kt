package com.algolia.instantsearch.core.number


public object NumberPresenterImpl : NumberPresenter<Number> {

    override fun invoke(number: Number?): String {
        return number?.toString() ?: "-"
    }
}