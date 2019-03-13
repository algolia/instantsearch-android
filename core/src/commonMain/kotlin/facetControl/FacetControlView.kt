package facetControl

interface FacetControlView<T> {
    fun setRefinement(value: T?)
    fun setSelected(isActive: Boolean)
    fun onClickRefinement(onClick: (T) -> Unit)
}