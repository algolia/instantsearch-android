package filter.facet

import com.algolia.client.model.search.FacetOrdering
import com.algolia.client.model.search.Facets
import com.algolia.client.model.search.SortRemainingBy
import com.algolia.client.model.search.Value
import com.algolia.instantsearch.filter.Facet
import com.algolia.instantsearch.filter.facet.dynamic.AttributedFacets
import com.algolia.instantsearch.filter.facet.dynamic.internal.facetsOrder
import kotlin.test.Test
import kotlin.test.assertEquals

class TestFacetsOrder {

    val facets = mapOf(
        "size" to listOf(
            Facet(value = "XS", count = 82),
            Facet(value = "L", count = 81),
            Facet(value = "XXXL", count = 73),
            Facet(value = "XXL", count = 71),
            Facet(value = "M", count = 67),
            Facet(value = "S", count = 67),
            Facet(value = "XL", count = 67),
        ),
        "brand" to listOf(
            Facet(value = "Dyson", count = 53),
            Facet(value = "Sony", count = 53),
            Facet(value = "Apple", count = 51),
            Facet(value = "Uniqlo", count = 43),
        ),
        "color" to listOf(
            Facet(value = "yellow", count = 65),
            Facet(value = "blue", count = 63),
            Facet(value = "red", count = 58),
            Facet(value = "violet", count = 55),
            Facet(value = "orange", count = 54),
            Facet(value = "green", count = 48),
        ),
        "country" to listOf(
            Facet(value = "Spain", count = 31),
            Facet(value = "Finland", count = 27),
            Facet(value = "Germany", count = 27),
            Facet(value = "UK", count = 26),
            Facet(value = "Italy", count = 25),
            Facet(value = "France", count = 23),
            Facet(value = "Denmark", count = 22),
            Facet(value = "USA", count = 19),
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
