package refinement


public interface RefinementListView<T> {

    fun setRefinements(refinements: List<Pair<T, Boolean>>)

    fun onClickRefinement(onClick: (T) -> Unit)
}