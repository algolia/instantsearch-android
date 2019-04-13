package refinement


fun <T> RefinementListPresenter<T>.connectWith(view: RefinementListView<T>) {
    listeners += (view::setRefinements)
}

fun <T> RefinementListViewModel<T>.connectWith(view: RefinementListView<T>) {
    view.onClickRefinement(::select)
}