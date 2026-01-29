package hierarchical

import com.algolia.client.model.search.FacetHits
import com.algolia.instantsearch.core.tree.Node
import com.algolia.instantsearch.hierarchical.internal.findNode
import shouldBeNull
import shouldEqual
import kotlin.test.Test

class TestExtensions {
    private fun FacetHits.toNode() = Node(this)

    private val category1 = FacetHits("Sport", "", 5).toNode()
    private val category11 = FacetHits("Sport > Bikes", "", 2).toNode()
    private val category12 = FacetHits("Sport > Bikesheds", "", 3).toNode()
    private val category11Custom = FacetHits("Sport | Bikes", "", 2).toNode()
    private val category12Custom = FacetHits("Sport | Bikesheds", "", 3).toNode()
    private val category2Mixed = FacetHits("Sport - Leisure", "", 5).toNode()
    private val category21Mixed = FacetHits("Sport - Leisure > Foo - Bar", "", 3).toNode()

    @Test
    fun findNodeMatchingShouldHit() {
        val nodes = mutableListOf(category1)

        nodes.findNode(category12.content) shouldEqual category1
    }

    @Test
    fun findNodePrefixShouldMiss() {
        val nodes = mutableListOf(category11)

        nodes.findNode(category12.content).shouldBeNull()
    }

    @Test
    fun findNodeMatchingCustomShouldHit() {
        val nodes = mutableListOf(category1)

        nodes.findNode(category12Custom.content) shouldEqual category1
    }

    @Test
    fun findNodeCustomShouldHitPrefix() {
        val nodes = mutableListOf(category2Mixed)

        nodes.findNode(category21Mixed.content) shouldEqual category2Mixed
    }

    @Test
    fun findNodeCustomShouldMissPrefix() {
        val nodes = mutableListOf(category11Custom)

        nodes.findNode(category12Custom.content).shouldBeNull()
    }
}
