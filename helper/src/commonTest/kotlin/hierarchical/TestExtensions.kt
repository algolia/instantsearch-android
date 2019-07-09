package hierarchical

import com.algolia.instantsearch.core.tree.Node
import com.algolia.instantsearch.helper.hierarchical.findNode
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet
import shouldBeNull
import shouldEqual
import kotlin.test.Test

class TestExtensions {
    private fun Facet.toNode() = Node(this)

    private val attribute = Attribute("foo")
    private val category1 = Facet("Sport", 5).toNode()
    private val category2 = Facet("Leisure", 1).toNode()
    private val category11 = Facet("Sport > Bikes", 2).toNode()
    private val category12 = Facet("Sport > Bikesheds", 3).toNode()
    private val category21 = Facet("Leisure > Books", 1).toNode()

    @Test
    fun findNodeShouldHitMatching() {
        val nodes = mutableListOf(category2)

        nodes.findNode(category21.content) shouldEqual category2
    }

    @Test
    fun findNodeShouldMissPrefix() {
        val nodes = mutableListOf(category11)

        nodes.findNode(category12.content).shouldBeNull()
    }
}