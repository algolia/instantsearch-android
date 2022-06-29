package tree

import com.algolia.instantsearch.core.tree.Node
import com.algolia.instantsearch.core.tree.Tree
import com.algolia.instantsearch.core.tree.asTree
import com.algolia.instantsearch.core.tree.findNode
import com.algolia.instantsearch.core.tree.toNodes
import kotlin.test.Test
import shouldBeNull
import shouldBeTrue
import shouldEqual

class TestHierarchicalNode {

    private fun String.toNode() = Node(this)

    private val category1 = "Furniture".toNode()
    private val category2 = "Book".toNode()
    private val category3 = "Clothing".toNode()
    private val category21 = "Book > Science Fiction".toNode()
    private val category22 = "Book > Romance".toNode()
    private val category31 = "Clothing > Men".toNode()
    private val category32 = "Clothing > Women".toNode()
    private val category311 = "Clothing > Men > Shirt".toNode()
    private val category312 = "Clothing > Men > Hats".toNode()
    private val category321 = "Clothing > Women > Shoes".toNode()
    private val category322 = "Clothing > Women > Bags".toNode()

    private val isMatchingString: (String, Node<String>, String) -> Boolean = { str, node, _ ->
        str.startsWith(node.content)
    }

    private fun List<Node<String>>.findNode(content: String) =
        findNode(content = content, isMatchingNode = isMatchingString)

    @Test
    fun findNodeShouldBeNull() {
        val nodes = mutableListOf<Node<String>>()

        nodes.findNode(category1.content).shouldBeNull()
    }

    @Test
    fun findNodeShouldMiss() {
        val nodes = mutableListOf(category1)

        nodes.findNode(category21.content).shouldBeNull()
    }

    @Test
    fun findFirstLevelNodeShouldHit() {
        val nodes = mutableListOf(category2)

        nodes.findNode(category21.content) shouldEqual category2
    }

    @Test
    fun findSecondLevelNodeShouldHit() {
        val node1 = category3.copy(children = mutableListOf(category31))
        val nodes = mutableListOf(node1)

        nodes.findNode(category311.content) shouldEqual category31
    }

    @Test
    fun toNodes() {
        val values = listOf(category1.content, category2.content, category21.content, category22.content)
        val nodes = values.toNodes(isMatchingString)
        nodes shouldEqual Tree(
            mutableListOf(category1, category2.copy(children = mutableListOf(category21, category22)))
        )
    }

    @Test
    fun toNodesWithSelectedNode() {
        val values = listOf(category1, category2, category21, category22).map { it.content }
        val nodes = values.toNodes(isMatchingString) {
            category21.content == it
        }

        nodes.children[1].children[0].isSelected.shouldBeTrue()
    }

    @Test
    fun asTree() {
        val nodes = listOf(
            category1, category2, category3,
            category21, category22, category31, category32,
            category311, category312, category321, category322
        )

        nodes.asTree(" . ", isMatchingString) shouldEqual Tree(
            mutableListOf(
                category1,
                category2.copy(children = mutableListOf(category21, category22)),
                category3.copy(
                    children = mutableListOf(
                        category31.copy(
                            children = mutableListOf(category311, category312)
                        ),
                        category32.copy(
                            children = mutableListOf(category321, category322)

                        )
                    )
                )
            )
        )
    }
}
