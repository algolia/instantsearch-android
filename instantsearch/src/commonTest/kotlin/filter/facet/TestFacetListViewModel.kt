package filter.facet

import com.algolia.client.model.search.FacetHits
import com.algolia.instantsearch.filter.facet.FacetListViewModel
import shouldEqual
import kotlin.test.Test

class TestFacetListViewModel {

    private val red = FacetHits("red", "", 1)
    private val green = FacetHits("green", "", 2)
    private val blue = FacetHits("blue", "", 3)

    @Test
    fun getFacetListItems() {
        val viewModel = FacetListViewModel().apply {
            items.value = listOf(red, green, blue)
            selections.value = setOf(blue.value)
        }

        viewModel.facets.value shouldEqual listOf(
            red to false,
            green to false,
            blue to true
        )
    }

    @Test
    fun getFacetListItemsWithPersistentSelection() {
        val viewModel = FacetListViewModel(persistentSelection = true).apply {
            items.value = listOf(red, green)
            selections.value = setOf(blue.value)
        }

        viewModel.facets.value shouldEqual listOf(
            blue.copy(count = 0) to true,
            red to false,
            green to false
        )
    }

    @Test
    fun getFacetListItemsWithoutPersistentSelection() {
        val viewModel = FacetListViewModel().apply {
            items.value = listOf(red, green)
            selections.value = setOf(blue.value)
        }

        viewModel.facets.value shouldEqual listOf(
            red to false,
            green to false
        )
    }
}
