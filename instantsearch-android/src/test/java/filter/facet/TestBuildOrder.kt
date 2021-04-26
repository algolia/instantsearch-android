package filter.facet

import com.algolia.instantsearch.helper.filter.facet.dynamic.internal.BuildOrder
import com.algolia.search.model.Attribute
import com.algolia.search.model.rule.AttributedFacets
import com.algolia.search.model.rule.FacetOrdering
import com.algolia.search.model.rule.OrderingRule
import com.algolia.search.model.rule.SortRule
import com.algolia.search.model.search.Facet
import kotlin.test.Test
import kotlin.test.assertEquals

class TestBuildOrder {

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
        return BuildOrder(facetOrdering, facets).invoke()
    }

    @Test
    fun testStrictFacetsOrder() {
        val facets = listOf("size", "brand", "color", "country").shuffled()
        val order = FacetOrdering(facets = OrderingRule(order = facets), facetValues = emptyMap())
        assertEquals(facets, withOrder(order).map { it.attribute.raw })
    }

    @Test
    fun testPartialFacetsOrder() {
        val facets = listOf("size", "country")
        val order = FacetOrdering(facets = OrderingRule(order = facets), facetValues = emptyMap())
        assertEquals(facets, withOrder(order).map { it.attribute.raw })
    }

    @Test
    fun testPartiallyStrictFacetsOrder() {
        val facets = listOf("size", "brand", "*")
        val order = FacetOrdering(facets = OrderingRule(order = facets), facetValues = emptyMap())
        assertEquals(listOf("size", "brand", "color", "country"), withOrder(order).map { it.attribute.raw })
    }

    @Test
    fun testHideFacet() {
        val order = FacetOrdering(facets = OrderingRule(hide = listOf("country")), facetValues = emptyMap())
        assertEquals(listOf("brand", "color", "size"), withOrder(order).map { it.attribute.raw })
    }

    @Test
    fun testSortFacetsByCount() {
        val order = FacetOrdering(facets = OrderingRule(sortBy = SortRule.Count), facetValues = emptyMap())
        assertEquals(listOf("brand", "color", "size", "country"), withOrder(order).map { it.attribute.raw })
    }

    @Test
    fun testStrictFacetValuesOrder() {
        val countries = listOf("UK", "France", "USA", "Germany", "Finland", "Denmark", "Italy", "Spain").shuffled()
        val order =
            FacetOrdering(facets = OrderingRule(), facetValues = mapOf("country" to OrderingRule(order = countries)))
        assertEquals(countries, withOrder(order).first { it.attribute.raw == "country" }.facets.map { it.value })
    }

    @Test
    fun testPartialFacetValuesOrder() {
        val countries = listOf("UK", "France", "USA").shuffled()
        val order =
            FacetOrdering(facets = OrderingRule(), facetValues = mapOf("country" to OrderingRule(order = countries)))
        assertEquals(countries, withOrder(order).first { it.attribute.raw == "country" }.facets.map { it.value })
    }

    @Test
    fun testPartiallyStrictFacetValuesOrder() {
        val countries = listOf("UK", "France", "USA", "*")
        val order =
            FacetOrdering(facets = OrderingRule(), facetValues = mapOf("country" to OrderingRule(order = countries)))
        assertEquals(
            countries.dropLast(1) + listOf("Germany", "Finland", "Denmark", "Italy", "Spain").sorted(),
            withOrder(order).first { it.attribute.raw == "country" }.facets.map { it.value })
    }

    @Test
    fun testHideFacetValues() {
        val countries = listOf("UK", "France", "USA", "Germany", "Finland", "Denmark", "Italy", "Spain").shuffled()
        val expectedResult = countries.filterNot { it == "USA" }.sorted()
        val order = FacetOrdering(facets = OrderingRule(), facetValues= mapOf("country" to OrderingRule(hide =  listOf("USA"))))
        assertEquals(expectedResult, withOrder(order).first { it.attribute.raw == "country" }.facets.map { it.value })
    }

    @Test
    fun testSortFacetValuesByCount() {
        val expectedFacetValues = facets[Attribute("country")]?.sortedBy { it.count }?.map { it.value }
        val order = FacetOrdering(facets = OrderingRule(), facetValues= mapOf("country" to OrderingRule(sortBy = SortRule.Count)))
        assertEquals(expectedFacetValues, withOrder(order).first { it.attribute.raw == "country" }.facets.map { it.value })
    }
}
