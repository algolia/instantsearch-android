package hierarchical

import com.algolia.instantsearch.core.tree.Node
import com.algolia.instantsearch.core.tree.Tree
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.toFilter
import com.algolia.instantsearch.helper.hierarchical.HierarchicalFilter
import com.algolia.instantsearch.helper.hierarchical.HierarchicalViewModel
import com.algolia.instantsearch.helper.hierarchical.connectFilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet
import shouldBeNull
import shouldEqual
import kotlin.test.Test

class TestHierarchicalConnectFilterState {

    private val separator = " > "
    private val category = Attribute("category")
    private val categoryLvl0 = Attribute("$category.lvl0")
    private val categoryLvl1 = Attribute("$category.lvl1")
    private val categories = listOf(
        categoryLvl0,
        categoryLvl1
    )
    private val facetShoes = Facet("Shoes", 3)
    private val facetShoesRunning = Facet("Shoes > Running", 2)
    private val facetShoesCocktail = Facet("Shoes > Cocktail", 1)
    private val facetBags = Facet("Bags", 1)
    private val filterShoes = facetShoes.toFilter(categoryLvl0)
    private val filterShoesRunning = facetShoesRunning.toFilter(categoryLvl1)
    private val filterBags = facetBags.toFilter(categoryLvl0)

    private val selection = facetShoesRunning.value
    private val tree: Tree<Facet> = Tree(
        mutableListOf(
            Node(facetBags),
            Node(facetShoes).copy(
                children = mutableListOf(
                    Node(facetShoesCocktail),
                    Node(facetShoesRunning)
                )
            )
        )
    )

    @Test
    fun connectShouldSetHierarchicalAttributes() {
        val viewModel = HierarchicalViewModel(category, categories, separator, tree)
        val filterState = FilterState()
        val connection = viewModel.connectFilterState(filterState)

        connection.connect()
        filterState.getHierarchicalFilters(category).shouldBeNull()
    }

    @Test
    fun onSelectionsComputedShouldUpdateFilterState() {
        val viewModel = HierarchicalViewModel(category, categories, separator, tree)
        val filterState = FilterState()
        val connection = viewModel.connectFilterState(filterState)

        connection.connect()
        viewModel.computeSelections(selection)
        filterState.getHierarchicalFilters(category) shouldEqual HierarchicalFilter(
            attributes = viewModel.hierarchicalAttributes,
            filter = filterShoesRunning,
            path = listOf(filterShoes, filterShoesRunning)
        )
    }

    @Test
    fun onFilterStateChangedShouldUpdateSelections() {
        val viewModel = HierarchicalViewModel(category, categories, separator, tree)
        val filterState = FilterState()
        val connection = viewModel.connectFilterState(filterState)

        connection.connect()
        filterState.notify {
            add(
                category,
                HierarchicalFilter(
                    viewModel.hierarchicalAttributes,
                    listOf(filterBags),
                    filterBags
                )
            )
        }
        viewModel.selections.value shouldEqual listOf(facetBags.value)
    }
}
