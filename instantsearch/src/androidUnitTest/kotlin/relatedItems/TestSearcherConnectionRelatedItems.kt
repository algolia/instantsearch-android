package relatedItems

import com.algolia.client.model.search.FacetFilters
import com.algolia.client.model.search.OptionalFilters
import com.algolia.instantsearch.relateditems.MatchingPattern
import com.algolia.instantsearch.relateditems.connectRelatedHitsView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName
import com.algolia.search.model.ObjectID
import mockClient
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class TestSearcherConnectionRelatedItems {

    private val client = mockClient()

    @Test
    fun connect() {
        val searcher = HitsSearcher(client, "Index")
        val adapter = mockHitsView<Product>()

        val patternBrand = MatchingPattern("attBrand", 1, Product::brand)
        val patternCategories = MatchingPattern("attCategories", 2, Product::categories)
        val matchingPatterns = listOf(patternBrand, patternCategories)

        val product = Product("objectID", "product", "brand", listOf("category1", "category2"))
        val connection = searcher.connectRelatedHitsView(adapter, product, matchingPatterns) {
            it.hits.deserialize(Product.serializer())
        }

        connection.connect()
        assertTrue(searcher.query.sumOrFiltersScores!!)
        val expectedFacetFilters =
            FacetFilters.of(listOf(FacetFilters.of(listOf(FacetFilters.of("objectID:-${product.objectID}")))))
        assertEquals(expectedFacetFilters, searcher.query.facetFilters)

        val expectedOptionalFilters = OptionalFilters.of(
            listOf(
                OptionalFilters.of(
                    listOf(
                        OptionalFilters.of("${patternBrand.attribute}:${product.brand}<score=${patternBrand.score}>")
                    )
                ),
                OptionalFilters.of(
                    listOf(
                        OptionalFilters.of("${patternCategories.attribute}:${product.categories[0]}<score=${patternCategories.score}>"),
                        OptionalFilters.of("${patternCategories.attribute}:${product.categories[1]}<score=${patternCategories.score}>")
                    )
                )
            )
        )
        assertEquals(expectedOptionalFilters, searcher.query.optionalFilters)
    }
}
