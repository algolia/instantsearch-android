package filter.facet

import com.algolia.instantsearch.filter.facet.dynamic.AttributedFacets
import com.algolia.instantsearch.filter.facet.dynamic.internal.facetsOrder
import com.algolia.search.model.Attribute
import com.algolia.search.model.rule.FacetOrdering
import com.algolia.search.model.rule.FacetValuesOrder
import com.algolia.search.model.rule.FacetsOrder
import com.algolia.search.model.rule.SortRule
import com.algolia.search.model.search.Facet
import kotlin.test.Test
import kotlin.test.assertEquals

class TestFacetsOrder {

    val facets = mapOf(
        Attribute("size") to listOf(
            Facet(value = "XS", count = 82),
            Facet(value = "L", count = 81),
            Facet(value = "XXXL", count = 73),
            Facet(value = "XXL", count = 71),
            Facet(value = "M", count = 67),
            Facet(value = "S", count = 67),
            Facet(value = "XL", count = 67),
        ),
        Attribute("brand") to listOf(
            Facet(value = "Dyson", count = 53),
            Facet(value = "Sony", count = 53),
            Facet(value = "Apple", count = 51),
            Facet(value = "Uniqlo", count = 43),
        ),
        Attribute("color") to listOf(
            Facet(value = "yellow", count = 65),
            Facet(value = "blue", count = 63),
            Facet(value = "red", count = 58),
            Facet(value = "violet", count = 55),
            Facet(value = "orange", count = 54),
            Facet(value = "green", count = 48),
        ),
        Attribute("country") to listOf(
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
        val order = FacetOrdering(facets = FacetsOrder(order = facets), values = emptyMap())
        assertEquals(facets, withOrder(order).map { it.attribute.raw })
    }

    @Test
    fun testPartialFacetsOrder() {
        val facets = listOf("size", "country")
        val order = FacetOrdering(facets = FacetsOrder(order = facets), values = emptyMap())
        assertEquals(facets, withOrder(order).map { it.attribute.raw })
    }

    @Test
    fun testStrictFacetValuesOrder() {
        val countries = listOf("UK", "France", "USA", "Germany", "Finland", "Denmark", "Italy", "Spain").shuffled()
        val order = FacetOrdering(
            facets = FacetsOrder(order = listOf("country")),
            values = mapOf(Attribute("country") to FacetValuesOrder(order = countries, sortRemainingBy = SortRule.Hidden))
        )
        assertEquals(countries, withOrder(order).first { it.attribute.raw == "country" }.facets.map { it.value })
    }

    @Test
    fun testPartialFacetValuesOrder() {
        val countries = listOf("UK", "France", "USA").shuffled()
        val order = FacetOrdering(
            facets = FacetsOrder(order = listOf("country")),
            values = mapOf(Attribute("country") to FacetValuesOrder(order = countries, sortRemainingBy = SortRule.Hidden))
        )
        assertEquals(countries, withOrder(order).first { it.attribute.raw == "country" }.facets.map { it.value })
    }

    @Test
    fun testPartiallyStrictFacetValuesOrder() {
        val countries = listOf("UK", "France", "USA")
        val order = FacetOrdering(
            facets = FacetsOrder(order = listOf("country")),
            values = mapOf(Attribute("country") to FacetValuesOrder(order = countries, sortRemainingBy = SortRule.Alpha))
        )
        assertEquals(countries + listOf("Germany", "Finland", "Denmark", "Italy", "Spain").sorted(), withOrder(order).first { it.attribute.raw == "country" }.facets.map { it.value })
    }

    @Test
    fun testSortFacetValuesByCount() {
        val expectedFacetValues = facets[Attribute("country")]?.sortedByDescending { it.count }?.map { it.value }
        val order = FacetOrdering(
            facets = FacetsOrder(order = listOf("country")),
            values = mapOf(Attribute("country") to FacetValuesOrder(order = emptyList(), sortRemainingBy = SortRule.Count))
        )
        assertEquals(expectedFacetValues, withOrder(order).first { it.attribute.raw == "country" }.facets.map { it.value })
    }
}
