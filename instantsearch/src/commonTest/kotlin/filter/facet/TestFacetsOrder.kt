package filter.facet

import com.algolia.client.model.search.FacetHits
import com.algolia.client.model.search.FacetOrdering
import com.algolia.client.model.search.Facets
import com.algolia.client.model.search.SortRemainingBy
import com.algolia.client.model.search.Value
import com.algolia.instantsearch.filter.facet.dynamic.AttributedFacets
import com.algolia.instantsearch.filter.facet.dynamic.internal.facetsOrder
import kotlin.test.Test
import kotlin.test.assertEquals

class TestFacetsOrder {

    val facets = mapOf(
        "size" to listOf(
            FacetHits(value = "XS", "", count = 82),
            FacetHits(value = "L", "", count = 81),
            FacetHits(value = "XXXL", "", count = 73),
            FacetHits(value = "XXL", "", count = 71),
            FacetHits(value = "M", "", count = 67),
            FacetHits(value = "S", "", count = 67),
            FacetHits(value = "XL", "", count = 67),
        ),
        "brand" to listOf(
            FacetHits(value = "Dyson", "", count = 53),
            FacetHits(value = "Sony", "", count = 53),
            FacetHits(value = "Apple", "", count = 51),
            FacetHits(value = "Uniqlo", "", count = 43),
        ),
        "color" to listOf(
            FacetHits(value = "yellow", "", count = 65),
            FacetHits(value = "blue", "", count = 63),
            FacetHits(value = "red", "", count = 58),
            FacetHits(value = "violet", "", count = 55),
            FacetHits(value = "orange", "", count = 54),
            FacetHits(value = "green", "", count = 48),
        ),
        "country" to listOf(
            FacetHits(value = "Spain", "", count = 31),
            FacetHits(value = "Finland", "", count = 27),
            FacetHits(value = "Germany", "", count = 27),
            FacetHits(value = "UK", "", count = 26),
            FacetHits(value = "Italy", "", count = 25),
            FacetHits(value = "France", "", count = 23),
            FacetHits(value = "Denmark", "", count = 22),
            FacetHits(value = "USA", "", count = 19),
        )
    )

    fun withOrder(facetOrdering: FacetOrdering): List<AttributedFacets> {
        return facetsOrder(facets = facets, facetOrdering = facetOrdering)
    }

    @Test
    fun testStrictFacetsOrder() {
        val facets = listOf("size", "brand", "color", "country").shuffled()
        val order = FacetOrdering(facets = Facets(order = facets), values = emptyMap())
        assertEquals(facets, withOrder(order).map { it.attribute })
    }

    @Test
    fun testPartialFacetsOrder() {
        val facets = listOf("size", "country")
        val order = FacetOrdering(facets = Facets(order = facets), values = emptyMap())
        assertEquals(facets, withOrder(order).map { it.attribute })
    }

    @Test
    fun testStrictFacetValuesOrder() {
        val countries = listOf("UK", "France", "USA", "Germany", "Finland", "Denmark", "Italy", "Spain").shuffled()
        val order = FacetOrdering(
            facets = Facets(order = listOf("country")),
            values = mapOf("country" to Value(order = countries, sortRemainingBy = SortRemainingBy.Hidden))
        )
        assertEquals(countries, withOrder(order).first { it.attribute == "country" }.facets.map { it.value })
    }

    @Test
    fun testPartialFacetValuesOrder() {
        val countries = listOf("UK", "France", "USA").shuffled()
        val order = FacetOrdering(
            facets = Facets(order = listOf("country")),
            values = mapOf("country" to Value(order = countries, sortRemainingBy = SortRemainingBy.Hidden))
        )
        assertEquals(countries, withOrder(order).first { it.attribute == "country" }.facets.map { it.value })
    }

    @Test
    fun testPartiallyStrictFacetValuesOrder() {
        val countries = listOf("UK", "France", "USA")
        val order = FacetOrdering(
            facets = Facets(order = listOf("country")),
            values = mapOf("country" to Value(order = countries, sortRemainingBy = SortRemainingBy.Alpha))
        )
        assertEquals(countries + listOf("Germany", "Finland", "Denmark", "Italy", "Spain").sorted(), withOrder(order).first { it.attribute == "country" }.facets.map { it.value })
    }

    @Test
    fun testSortFacetValuesByCount() {
        val expectedFacetValues = facets["country"]?.sortedByDescending { it.count }?.map { it.value }
        val order = FacetOrdering(
            facets = Facets(order = listOf("country")),
            values = mapOf("country" to Value(order = emptyList(), sortRemainingBy = SortRemainingBy.Count))
        )
        assertEquals(expectedFacetValues, withOrder(order).first { it.attribute == "country" }.facets.map { it.value })
    }
}
