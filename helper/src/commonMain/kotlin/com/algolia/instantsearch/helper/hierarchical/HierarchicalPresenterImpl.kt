package com.algolia.instantsearch.helper.hierarchical

import com.algolia.instantsearch.core.tree.asTree
import kotlin.jvm.JvmField
import kotlin.jvm.JvmOverloads

/**
 * A default Presenter for hierarchical trees, representing them as a flat list of nodes.
 */
public class HierarchicalPresenterImpl @JvmOverloads constructor(
    @JvmField
    val separator: String,
    @JvmField
    val comparator: Comparator<HierarchicalItem> = Comparator { a, b -> a.facet.value.compareTo(b.facet.value) }
) : HierarchicalPresenter<List<HierarchicalItem>> {

    override fun invoke(tree: HierarchicalTree): List<HierarchicalItem> {
        return present(tree)
    }

    /**
     * Presents the given tree as a flat list of nodes.
     */
    public fun present(tree: HierarchicalTree): List<HierarchicalItem> {
        return tree.asTree(comparator) { node, level, _ ->
            HierarchicalItem(
                node.content,
                node.content.value.split(separator)[level],
                level
            )
        }
    }
}