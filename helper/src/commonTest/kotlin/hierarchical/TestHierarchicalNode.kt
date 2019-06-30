package hierarchical

import com.algolia.instantsearch.helper.hierarchical.HierarchicalNode
import com.algolia.instantsearch.helper.hierarchical.asTree
import com.algolia.instantsearch.helper.hierarchical.findNode
import com.algolia.search.model.search.Facet
import shouldBeNull
import shouldEqual
import kotlin.test.Test


class TestHierarchicalNode {

    private fun Facet.toNode() = HierarchicalNode(this)

    private val category1 = Facet("Furniture", 1).toNode()
    private val category2 = Facet("Book", 2).toNode()
    private val category3 = Facet("Clothing", 4).toNode()
    private val category21 = Facet("Book > Science Fiction", 2).toNode()
    private val category22 = Facet("Book > Romance", 2).toNode()
    private val category31 = Facet("Clothing > Men", 1).toNode()
    private val category32 = Facet("Clothing > Women", 1).toNode()
    private val category311 = Facet("Clothing > Men > Shirt", 1).toNode()
    private val category312 = Facet("Clothing > Men > Hats", 1).toNode()
    private val category321 = Facet("Clothing > Women > Shoes", 1).toNode()
    private val category322 = Facet("Clothing > Women > Bags", 1).toNode()

    @Test
    fun findNodeShouldBeNull() {
        val nodes = mutableListOf<HierarchicalNode>()

        nodes.findNode(category1.facet).shouldBeNull()
    }

    @Test
    fun findNodeShouldMiss() {
        val nodes = mutableListOf(category1)

        nodes.findNode(category21.facet).shouldBeNull()
    }

    @Test
    fun findFirstLevelNodeShouldHit() {
        val nodes = mutableListOf(category2)

        nodes.findNode(category21.facet) shouldEqual category2
    }

    @Test
    fun findSecondLevelNodeShouldHit() {
        val node1 = category3.copy(children = mutableListOf(category31))
        val nodes = mutableListOf(node1)

        nodes.findNode(category311.facet) shouldEqual category31
    }

    @Test
    fun asTree() {
        val nodes = listOf(
            category1, category2, category3,
            category21, category22, category31, category32,
            category311, category312, category321, category322
        )

        nodes.asTree() shouldEqual listOf(
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
    }
}