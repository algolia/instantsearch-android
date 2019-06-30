package com.algolia.instantsearch.helper.hierarchical


public class HierarchicalPresenterImpl(
    val separator: String,
    val comparator: Comparator<HierarchicalItem> = Comparator { a, b -> a.facet.value.compareTo(b.facet.value) }
) : HierarchicalPresenter<List<HierarchicalItem>> {

    override fun invoke(tree: List<HierarchicalNode>): List<HierarchicalItem> {
        return tree.depth(comparator) { node, level, _ ->
            HierarchicalItem(
                node.facet,
                node.facet.value.split(separator)[level],
                level
            )
        }
    }
}