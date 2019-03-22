package numericControl

import helper.RefinementListener
import helper.RefinementListListener
import searcher.Searcher
import kotlin.properties.Delegates

class NumericControlModel<T> {
    var refinements by Delegates.observable(listOf<T>()) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            if (selected !in newValue) {
                onSelectedRefinement(null)
            }
            refinementsListeners.forEach { it(newValue) }
        }
    }
    var selected: T? = null

    val onSelectedRefinement = { refinement: T? ->
        if (selected != refinement) {
            selected = refinement
            selectionListeners.forEach { it(refinement) }
        }
    }

    val refinementsListeners = mutableListOf<RefinementListListener<T>>()
    val selectionListeners = mutableListOf<RefinementListener<T?>>()
}

fun <T> NumericControlModel<T>.connectSearcher(searcher: Searcher) {
    selectionListeners += { searcher.search() }
}

fun <T> NumericControlModel<T>.connectView(view: NumericControlView<T>) {
    refinementsListeners += (view::setRefinements)
    selectionListeners += { view.setSelected(it) }
    view.onClickRefinement(::onSelectedRefinement.get())
}

fun <T> NumericControlModel<T>.connectViews(views: List<NumericControlView<T>>) {
    views.forEach(::connectView)
}