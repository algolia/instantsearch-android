package com.algolia.instantsearch.core.number


public fun <T : Number> NumberViewModel<T>.connectView(
    view: NumberView<T>,
    presenter: NumberPresenter<T> = NumberPresenterImpl
) {
    val onItemChanged : (T?) -> Unit = { item ->
        view.setItem(presenter(item))
    }

    onItemChanged(item)
    this.onItemChanged += onItemChanged
    view.setComputation { computeNumber(it(item)) }
}