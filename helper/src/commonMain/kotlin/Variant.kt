package model

import com.algolia.search.exception.EmptyStringException

public data class Variant(/*override */val raw: String) /*: Raw<String> */{

    init {
        if (raw.isEmpty()) throw EmptyStringException("Variant")
    }

    override fun toString(): String {
        return raw
    }
}