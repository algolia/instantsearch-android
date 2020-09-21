package tree

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.tree.Node
import com.algolia.instantsearch.core.tree.Tree
import com.algolia.instantsearch.core.tree.TreePresenter
import com.algolia.instantsearch.core.tree.TreeView
import com.algolia.instantsearch.core.tree.TreeViewModel
import com.algolia.instantsearch.core.tree.asTree
import com.algolia.instantsearch.core.tree.connectView
import shouldBeEmpty
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test

class TestTreeConnectView {

    private val bags = "Bags"
    private val shoes = "Shoes"
    private val shoesCocktail = "Shoes > Cocktail"
    private val shoesRunning = "Shoes > Running"
    private val expectedItems = listOf(bags, shoes, shoesCocktail, shoesRunning)
    private val expectedSelection = shoesRunning

    private val presenter: TreePresenter<String, List<String>> = {
        it.asTree(Comparator { a, b -> a.compareTo(b) }) { node, _, _ ->
            node.content
        }
    }
    private val tree: Tree<String> = Tree(
        mutableListOf(
            Node(bags),
            Node(shoes).copy(
                children = mutableListOf(
                    Node(shoesRunning),
                    Node(shoesCocktail)
                )
            )
        )
    )

    private class MockTreeView : TreeView<String, List<String>> {

        internal var items: List<String> = listOf()
        override var onSelectionChanged: Callback<String>? = null

        override fun setTree(tree: List<String>) {
            items = tree
        }
    }

    private inner class MockTreeViewModel(tree: Tree<String>) : TreeViewModel<String, String>(tree) {

        var selection: String = ""

        override fun computeSelections(key: String) {
            selection = key
        }
    }

    @Test
    fun connectShouldCallSetItem() {
        val view = MockTreeView()
        val viewModel = MockTreeViewModel(tree)
        val connection = viewModel.connectView(view, presenter)

        viewModel.selection = shoesRunning
        connection.connect()
        view.items shouldEqual expectedItems
    }

    @Test
    fun onItemChangedShouldCallSetItem() {
        val view = MockTreeView()
        val viewModel = MockTreeViewModel(Tree())
        val connection = viewModel.connectView(view, presenter)

        connection.connect()
        view.items.shouldBeEmpty()
        viewModel.tree.value = tree
        view.items shouldEqual expectedItems
    }

    @Test
    fun onClickShouldCallComputeSelections() {
        val view = MockTreeView()
        val viewModel = MockTreeViewModel(tree)
        val connection = viewModel.connectView(view, presenter)

        connection.connect()
        view.onSelectionChanged.shouldNotBeNull()
        view.onSelectionChanged!!(shoesRunning)
        viewModel.selection shouldEqual expectedSelection
    }
}
