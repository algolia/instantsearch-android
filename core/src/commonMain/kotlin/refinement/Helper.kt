package refinement

import helper.Converter
import searcher.Searcher


fun <T> RefinementListViewModel<T>.connectSearcher(searcher: Searcher) {
    selectionListeners += { _, _ -> searcher.search() }
}

fun <T> RefinementListViewModel<T>.connectView(view: RefinementView<T>) {
    dataListeners += (view::setRefinements)
    selectionListeners += { _, newList -> view.setSelected(newList) }
    view.setOnClickRefinement(::onSelectedRefinement.get())
    view.setRefinements(refinements)
    view.setSelected(selected)
}

fun <T, R> RefinementListViewModel<T>.connectView(view: RefinementView<R>, converter: Converter<T, R>) {
    dataListeners += { view.setRefinements(converter.convertIn(it)) }
    selectionListeners += { _, newList -> view.setSelected(converter.convertIn(newList)) }
    view.setOnClickRefinement { refinement, isActive ->
        onSelectedRefinement(converter.convertOut(refinement), isActive)
    }
}

fun <T> RefinementListViewModel<T>.connectViews(views: List<RefinementView<T>>) {
    views.forEach(::connectView)
}

fun <T, R> RefinementListViewModel<T>.connectViews(views: List<RefinementView<R>>, converter: Converter<T, R>) {
    views.forEach { connectView(it, converter) }
}

