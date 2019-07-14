package com.algolia.instantsearch.core.map


public sealed class MapEvent<K, V> {

    protected abstract val map: Map<K, V>

    public abstract fun get(): Map<K, V>

    public data class Remove<K, V>(
        protected override val map: Map<K, V>,
        val key: K
    ) : MapEvent<K, V>() {

        override fun get(): Map<K, V> {
            return map.toMutableMap().apply { remove(key) }
        }
    }

    public data class Add<K, V>(
        protected override val map: Map<K, V>,
        val entry: Pair<K, V>
    ) : MapEvent<K, V>() {

        override fun get(): Map<K, V> {
            return map.toMutableMap().apply { put(entry.first, entry.second) }
        }
    }

    public data class Clear<K, V>(
        protected override val map: Map<K, V>
    ) : MapEvent<K, V>() {

        override fun get(): Map<K, V> {
            return map.toMutableMap().apply { clear() }
        }
    }
}