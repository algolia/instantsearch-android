package relatedItems

import com.algolia.instantsearch.helper.relateditems.MatchingPattern
import com.algolia.search.model.Attribute
import com.algolia.search.model.ObjectID
import kotlin.test.Test
import kotlin.test.assertEquals

class TestMachingPattern {

    @Test
    fun matchingPattern_property() {
        val objectID = ObjectID("objectID")
        val name = "product"
        val brand = "brand"
        val categories = listOf("category")
        val product = Product(objectID, name, brand, categories)

        val patternBrand = MatchingPattern(Attribute("brand"), 1, Product::brand)
        val patternCategories = MatchingPattern(Attribute("categories"), 1, Product::categories)

        assertEquals(brand, patternBrand.property.get(product))
        assertEquals(categories, patternCategories.property.get(product))
    }
}
