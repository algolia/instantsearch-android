package refinement


interface RefinementView<T> {

    fun setRefinements(refinements: List<T>)
    fun setSelected(refinements: List<T>)
    fun setOnClickRefinement(onClick: (T) -> Unit)
}