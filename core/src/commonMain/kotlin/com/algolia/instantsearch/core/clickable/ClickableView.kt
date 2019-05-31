package com.algolia.instantsearch.core.clickable


public interface ClickableView<T> {

    public var onClick: ((T) -> Unit)?
}
