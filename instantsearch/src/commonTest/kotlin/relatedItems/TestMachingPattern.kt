package relatedItems

import com.algolia.instantsearch.relateditems.MatchingPattern
import kotlin.test.Test
import kotlin.test.assertEquals

class TestMachingPattern {

    @Test
    fun matchingPattern_property() {
        val objectID = "objectID"
        val name = "product"
        val brand = "brand"
        val categories = listOf("category")
        val product = Product(objectID, name, brand, categories)

        val patternBrand = MatchingPattern("brand", 1, Product::brand)
        val patternCategories = MatchingPattern("categories", 1, Product::categories)

        assertEquals(brand, patternBrand.property.get(product))
        assertEquals(categories, patternCategories.property.get(product))
    }
}
