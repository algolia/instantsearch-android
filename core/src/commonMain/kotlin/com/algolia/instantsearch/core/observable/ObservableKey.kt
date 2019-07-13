package com.algolia.instantsearch.core.observable


public data class ObservableKey(val name: String) {

    public companion object {

        public operator fun invoke(observable: ObservableItem<*>): ObservableKey {
            return ObservableKey(observable.listeners.size.toString())
        }

        public operator fun invoke(observable: ObservableEvent<*>): ObservableKey {
            return ObservableKey(observable.listeners.size.toString())
        }
    }
}