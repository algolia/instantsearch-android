package tree

import com.algolia.instantsearch.core.tree.Node
import com.algolia.instantsearch.core.tree.Tree
import com.algolia.instantsearch.core.tree.TreePresenter
import com.algolia.instantsearch.core.tree.asTree
import shouldEqual
import kotlin.test.Test

class TestTreePresenter {

    private val book = "Book"
    private val bookScienceFiction = "$book > Science Fiction"
    private val bookRomance = "$book > Romance"
    private val clothing = "Clothing"
    private val clothingMen = "$clothing > Men"
    private val clothingMenHats = "$clothingMen > Hats"
    private val clothingMenShirts = "$clothingMen > Shirts"
    private val clothingWomen = "$clothing > Women"
    private val clothingWomenBags = "$clothingWomen > Bags"
    private val clothingWomenShoes = "$clothingWomen > Shoes"
    private val furniture = "Furniture"

    private val presenter: TreePresenter<String, List<String>> = {
        val comparator = Comparator<String> { a, b -> a.compareTo(b) }
        it.asTree(comparator) { node, _, _ -> node.content }
    }
    private val tree = Tree(
        mutableListOf(
            Node(
                book,
                mutableListOf(
                    Node(bookScienceFiction),
                    Node(bookRomance)
                )
            ),
            Node(furniture),
            Node(
                clothing,
                mutableListOf(
                    Node(
                        clothingMen,
                        mutableListOf(
                            Node(clothingMenHats),
                            Node(clothingMenShirts)
                        )
                    ),
                    Node(
                        clothingWomen,
                        mutableListOf(
                            Node(clothingWomenShoes),
                            Node(clothingWomenBags)
                        )
                    )
                )
            )
        )
    )

    @Test
    fun presenterShouldSortTree() {
        presenter(tree) shouldEqual listOf(
            book, bookRomance, bookScienceFiction,
            clothing, clothingMen, clothingMenHats, clothingMenShirts,
            clothingWomen, clothingWomenBags, clothingWomenShoes,
            furniture
        )
    }
}
