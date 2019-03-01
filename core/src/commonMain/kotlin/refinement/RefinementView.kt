package refinement


interface RefinementView<T> {

    fun setRefinements(refinements: List<T>)
    fun setSelected(refinements: List<T>)
    fun onClickRefinement(onClick: (T) -> Unit)
}