package hierarchicalMenu

import helper.Hierarchy


interface HierarchicalView<T> {

    /**
     * Called to update the Hierarchy displayed.
     */
    fun setHierarchy(hierarchy: Hierarchy<T>)

    /**
     * Called to update the current refinement.
     */
    fun setSelected(refinement: T?)

    /**
     * Called when the refinement is clicked.
     */
    fun onClickRefinement(onClick: (T) -> Unit)
}