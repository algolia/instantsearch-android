package com.algolia.instantsearch.hierarchical

import com.algolia.instantsearch.core.tree.asTree

public class DefaultHierarchicalPresenter(
    public val separator: String,
    public val comparator: Comparator<HierarchicalItem> = Comparator { a, b -> a.facet.value.compareTo(b.facet.value) },
) : HierarchicalPresenter<List<HierarchicalItem>> {

    override fun invoke(tree: HierarchicalTree): List<HierarchicalItem> {
        return tree.asTree(comparator) { node, level, _ ->
            HierarchicalItem(
                facet = node.content,
                displayName = node.content.value.split(separator)[level],
                level = level,
                isSelected = node.isSelected
            )
        }
    }
}

@Deprecated(
    message = "use DefaultHierarchicalPresenter instead",
    replaceWith = ReplaceWith("DefaultHierarchicalPresenter")
)
public typealias HierarchicalPresenterImpl = DefaultHierarchicalPresenter
