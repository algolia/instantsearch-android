package hierarchical

import com.algolia.instantsearch.core.tree.Node
import com.algolia.instantsearch.core.tree.Tree
import com.algolia.instantsearch.hierarchical.HierarchicalItem
import com.algolia.instantsearch.hierarchical.DefaultHierarchicalPresenter
import com.algolia.instantsearch.hierarchical.HierarchicalViewModel
import com.algolia.instantsearch.filter.Facet
import shouldEqual
import kotlin.test.Test

class TestHierarchicalPresenter {

    private val separator = " > "
    private val presenter = DefaultHierarchicalPresenter(separator)
    private val attribute = "type"
    private val facetShoes = Facet("Shoes", 3)
    private val facetShoesRunning = Facet("Shoes > Running", 2)
    private val facetShoesCocktail = Facet("Shoes > Cocktail", 1)
    private val facetBags = Facet("Bags", 1)
    private val tree: Tree<Facet> = Tree(
        mutableListOf(
            Node(facetBags),
            Node(facetShoes).copy(
                children = mutableListOf(
                    Node(facetShoesRunning),
                    Node(facetShoesCocktail)
                )
            )
        )
    )

    private fun Facet.toDisplayName() = value.split(separator).last()

    @Test
    fun presenterShouldTransformAndSortHierarchy() {
        val viewModel = HierarchicalViewModel(attribute, listOf("foo"), separator, tree)

        presenter(viewModel.tree.value) shouldEqual listOf(
            HierarchicalItem(facetBags, facetBags.toDisplayName(), 0),
            HierarchicalItem(facetShoes, facetShoes.toDisplayName(), 0),
            HierarchicalItem(facetShoesCocktail, facetShoesCocktail.toDisplayName(), 1),
            HierarchicalItem(facetShoesRunning, facetShoesRunning.toDisplayName(), 1)
        )
    }
}
