package com.algolia.instantsearch.helper.filter.numeric.comparison


public object NumberPresenterImpl : NumberPresenter<Number> {

    override fun invoke(number: Number?): String {
        return number?.toString() ?: "-"
    }
}