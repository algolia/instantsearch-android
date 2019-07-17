package com.algolia.instantsearch.core.item

import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.observable.ObservableItem


public fun <I, O> ItemViewModel<I>.connectView(view: ItemView<O>, presenter: Presenter<I, O>) {
    val onItemChanged: (I) -> Unit = { item ->
        view.setItem(presenter(item))
    }

    onItemChanged(item)
    this.onItemChanged += onItemChanged
}

public fun <I, O> ObservableItem<I>.connectView(
    view: ItemView<O>,
    presenter: Presenter<I, O>
) {
    subscribePast { view.setItem(presenter(it)) }
}