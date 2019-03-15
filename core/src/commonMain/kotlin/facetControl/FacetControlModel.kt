package facetControl

import helper.RefinementListener
import searcher.Searcher
import kotlin.properties.Delegates

class FacetControlModel<T> {
    var refinement: T? by Delegates.observable(null as T?) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            onSelectedRefinement(null)
            refinementListeners.forEach { it(newValue) }
        }
    }
    var selected: T? = null


    val onSelectedRefinement = { facet: T? ->
        if (selected != facet) {
            selected = facet
            selectionListeners.forEach { it(facet) }
        }
    }

    val refinementListeners = mutableListOf<RefinementListener<T>>()
    val selectionListeners = mutableListOf<RefinementListener<T>>()
}

fun <T> FacetControlModel<T>.connectSearcher(searcher: Searcher) {
    selectionListeners += { searcher.search() }
}

fun <T> FacetControlModel<T>.connectView(view: FacetControlView<T>) {
    refinementListeners += (view::setRefinement)
    selectionListeners += { view.setSelected((it != null)) }
    view.onClickRefinement(::onSelectedRefinement.get())
}

fun <T> FacetControlModel<T>.connectViews(views: List<FacetControlView<T>>) {
    views.forEach(::connectView)
}