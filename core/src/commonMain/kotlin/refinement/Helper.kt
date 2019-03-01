package refinement

import helper.Converter
import searcher.Searcher


fun <T> RefinementModel<T>.connectViews(views: List<RefinementView<T>>) {
    views.forEach(::connectView)
}

fun <T> RefinementModel<T>.connectSearcher(searcher: Searcher) {
    selectedListeners += { searcher.search() }
}

fun <T, R> RefinementModel<T>.connectView(view: RefinementView<R>, converter: Converter<T, R>) {
    refinementListeners += { view.setRefinements(converter.convertIn(it)) }
    selectedListeners += { view.setSelected(converter.convertIn(it)) }
    view.onClickRefinement {
        onSelectedRefinement(converter.convertOut(it))
    }
}

fun <T> RefinementModel<T>.connectView(view: RefinementView<T>) {
    refinementListeners += (view::setRefinements)
    selectedListeners += (view::setSelected)
    view.onClickRefinement(::onSelectedRefinement.get())
}