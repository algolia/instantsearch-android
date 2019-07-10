package hierarchical

import com.algolia.instantsearch.core.tree.Node
import com.algolia.instantsearch.core.tree.Tree
import com.algolia.instantsearch.core.tree.TreeView
import com.algolia.instantsearch.helper.hierarchical.HierarchicalItem
import com.algolia.instantsearch.helper.hierarchical.HierarchicalPresenterImpl
import com.algolia.instantsearch.helper.hierarchical.HierarchicalViewModel
import com.algolia.instantsearch.helper.hierarchical.connectView
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet
import shouldBeEmpty
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestHierarchicalConnectView {

    private val selections = listOf("Shoes > Running")
    private val separator = " > "
    private val presenter = HierarchicalPresenterImpl(separator)

    private val facetShoes = Facet("Shoes", 3)
    private val facetShoesRunning = Facet("Shoes > Running", 2)
    private val facetShoesCocktail = Facet("Shoes > Cocktail", 1)
    private val facetBags = Facet("Bags", 1)
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

    private fun Facet.toDisplayName() = value.split(separator).last()

    private val expectedItems = listOf(
        HierarchicalItem(facetBags, facetBags.toDisplayName(), 0),
        HierarchicalItem(facetShoes, facetShoes.toDisplayName(), 0),
        HierarchicalItem(facetShoesCocktail, facetShoesCocktail.toDisplayName(), 1),
        HierarchicalItem(facetShoesRunning, facetShoesRunning.toDisplayName(), 1)
    )
    private val expectedSelection = listOf(facetShoes.value, facetShoesRunning.value)
    private val attributes = listOf(
        Attribute("category.lvl0"),
        Attribute("category.lvl1")
    )

    private class MockTreeView : TreeView<List<HierarchicalItem>> {
        internal var items: List<HierarchicalItem> = listOf()

        override var onClick: ((String) -> Unit)? = null

        override fun setItem(item: List<HierarchicalItem>) {
            items = item
        }
    }

    @Test
    fun connectShouldCallSetItem() {
        val view = MockTreeView()
        val viewModel = HierarchicalViewModel(attributes, separator, tree)

        viewModel.selections = selections
        viewModel.connectView(view, presenter)
        view.items shouldEqual expectedItems
    }

    @Test
    fun onItemChangedShouldCallSetItem() {
        val view = MockTreeView()
        val viewModel = HierarchicalViewModel(attributes, separator)

        viewModel.connectView(view, presenter)
        viewModel.item = tree.copy(children = mutableListOf())
        view.items shouldEqual listOf()
    }

    @Test
    fun onClickShouldCallOnSelectionsComputed() {
        val view = MockTreeView()
        val viewModel = HierarchicalViewModel(attributes, separator)

        viewModel.onSelectionsComputed += { list ->
            viewModel.selections = list.map { it.second }
        }
        viewModel.connectView(view, presenter)
        view.items.shouldBeEmpty()
        view.onClick.shouldNotBeNull()
        view.onClick!!(selections.first())
        viewModel.selections shouldEqual expectedSelection
    }
}