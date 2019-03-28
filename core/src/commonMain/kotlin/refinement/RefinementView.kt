package refinement


interface RefinementView<T> {

    fun setRefinements(refinements: List<T>)
    fun setSelected(refinements: List<T>)
    /**
     * Sets the listener when a refinement is clicked.
     * @param onClick a function that takes a refinement and its active state (true = enabled).
     */
    fun setOnClickRefinement(onClick: (T, Boolean) -> Unit)
}