package com.algolia.instantsearch.helper.hierarchical

import com.algolia.instantsearch.core.tree.asTree


public class HierarchicalPresenterImpl(
    val separator: String,
    val comparator: Comparator<HierarchicalItem> = Comparator { a, b -> a.facet.value.compareTo(b.facet.value) }
) : HierarchicalPresenter<List<HierarchicalItem>> {

    override fun invoke(tree: HierarchicalTree): List<HierarchicalItem> {
        return tree.asTree(comparator) { node, level, _ ->
            HierarchicalItem(
                node.content,
                node.content.value.split(separator)[level],
                level
            )
        }
    }
}