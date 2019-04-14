package refinement


public fun <T> RefinementListPresenter<T>.connectWith(view: RefinementListView<T>) {
    listeners += (view::setRefinements)
}

public fun <T> RefinementListViewModel<T>.connectWith(view: RefinementListView<T>) {
    view.onClickRefinement { select(it) }
}