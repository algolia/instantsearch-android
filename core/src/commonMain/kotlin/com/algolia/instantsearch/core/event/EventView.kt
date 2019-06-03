package com.algolia.instantsearch.core.event


public interface EventView<T> {

    public var onClick: ((T) -> Unit)?
}
