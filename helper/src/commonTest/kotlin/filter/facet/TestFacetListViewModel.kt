package filter.facet

import com.algolia.instantsearch.helper.filter.facet.FacetListViewModel
import com.algolia.search.model.search.Facet
import shouldEqual
import kotlin.test.Test


class TestFacetListViewModel  {

    private val red = Facet("red", 1)
    private val green = Facet("green", 2)
    private val blue = Facet("blue", 3)

    @Test
    fun getFacetListItems() {
        val viewModel = FacetListViewModel().apply {
            item = listOf(red, green, blue)
            selections = setOf(blue.value)
        }

        viewModel.getFacetListItems() shouldEqual listOf(
            red to false,
            green to false,
            blue to true
        )
    }

    @Test
    fun getFacetListItemsWithPersistentSelection() {
        val viewModel = FacetListViewModel(persistentSelection = true).apply {
            item = listOf(red, green)
            selections = setOf(blue.value)
        }

        viewModel.getFacetListItems() shouldEqual listOf(
            blue.copy(count = 0) to true,
            red to false,
            green to false
        )
    }

    @Test
    fun getFacetListItemsWithoutPersistentSelection() {
        val viewModel = FacetListViewModel().apply {
            item = listOf(red, green)
            selections = setOf(blue.value)
        }

        viewModel.getFacetListItems() shouldEqual listOf(
            red to false,
            green to false
        )
    }
}