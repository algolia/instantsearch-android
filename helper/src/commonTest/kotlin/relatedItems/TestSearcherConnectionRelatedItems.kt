package relatedItems

import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.helper.relateditems.MatchingPattern
import com.algolia.instantsearch.helper.relateditems.connectRelatedHitsView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.helper.deserialize
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.ObjectID
import mockClient
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestSearcherConnectionRelatedItems {

    private val client = mockClient()
    private val index = client.initIndex(IndexName("Index"))

    @Test
    fun connect() {
        val searcher = SearcherSingleIndex(index)
        val adapter = mockHitsView<Product>()

        val patternBrand = MatchingPattern(Attribute("attBrand"), 1, Product::brand)
        val patternCategories = MatchingPattern(Attribute("attCategories"), 1, Product::categories)
        val matchingPatterns = listOf(patternBrand, patternCategories)

        val product = Product(ObjectID("objectID"), "product", "brand", listOf("category1", "category2"))
        val connection = searcher.connectRelatedHitsView(adapter, product, matchingPatterns) {
            it.hits.deserialize(Product.serializer())
        }

        connection.connect()
        assertTrue(searcher.query.sumOrFiltersScores!!)
        assertEquals(listOf(listOf("objectID:-${product.objectID}")), searcher.query.facetFilters)
        assertEquals(
            listOf(
                listOf("${patternBrand.attribute}:${product.brand}<score=${patternBrand.score}>"),
                listOf(
                    "${patternCategories.attribute}:${product.categories[0]}<score=${patternCategories.score}>",
                    "${patternCategories.attribute}:${product.categories[1]}<score=${patternCategories.score}>"
                )
            ),
            searcher.query.optionalFilters
        )

        println(searcher.query)
    }

    private fun <T> mockHitsView(): HitsView<T> {
        return object : HitsView<T> {
            override fun setHits(hits: List<T>) {
                // Ignored.
            }
        }
    }
}
