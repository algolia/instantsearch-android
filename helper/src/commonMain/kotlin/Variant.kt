package model

import com.algolia.search.exception.EmptyStringException


data class Variant(val name: String) {

    init {
        if (name.isEmpty()) throw EmptyStringException("Variant")
    }

    override fun toString(): String = name
}
