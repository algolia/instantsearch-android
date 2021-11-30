package relatedItems

import com.algolia.instantsearch.core.hits.HitsView

internal fun <T> mockHitsView(): HitsView<T> {
    return object : HitsView<T> {
        override fun setHits(hits: List<T>) {
            // Ignored.
        }
    }
}
