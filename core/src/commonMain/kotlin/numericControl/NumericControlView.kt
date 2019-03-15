package numericControl

interface NumericControlView<T> {
    fun setRefinements(refinements: List<T>)
    fun setSelected(refinement: T?)
    fun setDefaultChoice(choice: String) // Displayed when the refinement is null
    fun onClickRefinement(onClick: (T) -> Unit)
}