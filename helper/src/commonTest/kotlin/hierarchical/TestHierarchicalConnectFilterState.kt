package hierarchical

import com.algolia.instantsearch.core.tree.Node
import com.algolia.instantsearch.core.tree.Tree
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.toFilter
import com.algolia.instantsearch.helper.hierarchical.HierarchicalViewModel
import com.algolia.instantsearch.helper.hierarchical.connectFilterState
import com.algolia.instantsearch.helper.hierarchical.hierarchicalGroupName
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet
import shouldEqual
import kotlin.test.Test


class TestHierarchicalConnectFilterState {

    private val separator = " > "
    private val category = Attribute("category")
    private val categories = listOf(
        Attribute("$category.lvl0"),
        Attribute("$category.lvl1")
    )
    private val groupID = FilterGroupID(hierarchicalGroupName)

    private val facetShoes = Facet("Shoes", 3)
    private val facetShoesRunning = Facet("Shoes > Running", 2)
    private val facetShoesCocktail = Facet("Shoes > Cocktail", 1)
    private val facetBags = Facet("Bags", 1)
    private val filterShoes = facetShoes.toFilter(categories[0])
    private val filterShoesRunning = facetShoesRunning.toFilter(categories[1])
    private val filterBags = facetBags.toFilter(categories[0])

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
    private val expectedFilterState = FilterState(
        mapOf(
            groupID to setOf(
                filterShoesRunning
            )
        )
    ).apply { hierarchicalFilters = listOf(filterShoes, filterShoesRunning) }

    @Test
    fun connectShouldSetHierarchicalAttributes() {
        val viewModel = HierarchicalViewModel(categories, separator, tree)
        val filterState = FilterState()
        val connection = viewModel.connectFilterState(filterState)

        connection.connect()
        filterState.hierarchicalAttributes shouldEqual viewModel.hierarchicalAttributes
    }

    @Test
    fun onSelectionsComputedShouldUpdateFilterState() {
        val viewModel = HierarchicalViewModel(categories, separator, tree)
        val filterState = FilterState()
        val connection = viewModel.connectFilterState(filterState)

        connection.connect()
        viewModel.computeSelections(selection)
        filterState.filters.value shouldEqual expectedFilterState.filters.value
        filterState.hierarchicalFilters shouldEqual expectedFilterState.hierarchicalFilters
    }

    @Test
    fun onFilterStateChangedShouldUpdateSelections() {
        val viewModel = HierarchicalViewModel(categories, separator, tree)
        val filterState = FilterState()
        val connection = viewModel.connectFilterState(filterState)

        connection.connect()
        filterState.notify {
            remove(groupID, filterShoesRunning)
            add(groupID, filterBags)
        }
        viewModel.selections.value shouldEqual listOf(facetBags.value)
    }
}