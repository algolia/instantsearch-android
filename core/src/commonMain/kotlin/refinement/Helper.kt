package refinement

import helper.Converter
import searcher.Searcher


fun <T> RefinementListViewModel<T>.connectSearcher(searcher: Searcher) {
    selectionListeners += { searcher.search() }
}

fun <T> RefinementListViewModel<T>.connectView(view: RefinementView<T>) {
    refinementListeners += (view::setRefinements)
    selectionListeners += (view::setSelected)
    view.setOnClickRefinement(::onSelectedRefinement.get())
    view.setRefinements(refinements)
    view.setSelected(selected)
}

fun <T, R> RefinementListViewModel<T>.connectView(view: RefinementView<R>, converter: Converter<T, R>) {
    refinementListeners += { view.setRefinements(converter.convertIn(it)) }
    selectionListeners += { view.setSelected(converter.convertIn(it)) }
    view.setOnClickRefinement {
        onSelectedRefinement(if (it == null) it else converter.convertOut(it))
    }
}

fun <T> RefinementListViewModel<T>.connectViews(views: List<RefinementView<T>>) {
    views.forEach(::connectView)
}

fun <T, R> RefinementListViewModel<T>.connectViews(views: List<RefinementView<R>>, converter: Converter<T, R>) {
    views.forEach { connectView(it, converter) }
}

