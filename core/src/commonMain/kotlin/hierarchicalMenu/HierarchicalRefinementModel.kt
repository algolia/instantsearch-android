package hierarchicalMenu

import helper.Hierarchy
import helper.HierarchyRefinementListener
import helper.RefinementListener
import searcher.Searcher
import kotlin.properties.Delegates


open class HierarchicalRefinementModel<T> {

    var hierarchicalRefinements: Hierarchy<T> by Delegates.observable(mapOf()) { _, oldValue, newValue ->
        oldValue.entries.forEach { _ ->
            // If selected is absent from new Hierarchy, change it to null and notify listeners
            if (selected !in newValue.entries.flatMap { it.value }) onSelectedRefinement(null)
        }
        if (oldValue != newValue) hierarchyListeners.forEach { it(newValue) }
    }

    var selected: T? = null

    val onSelectedRefinement = { refinement: T? ->
        selected = if (selected != refinement) refinement else null
        selectionListeners.forEach { it(selected) }
    }

    val hierarchyListeners = mutableListOf<HierarchyRefinementListener<T>>()
    val selectionListeners = mutableListOf<RefinementListener<T?>>()
}

fun <T> HierarchicalRefinementModel<T>.connectSearcher(searcher: Searcher) {
    selectionListeners += { searcher.search() }
}

fun <T> HierarchicalRefinementModel<T>.connectView(view: HierarchicalView<T>) {
    hierarchyListeners += (view::setHierarchy)
    selectionListeners += (view::setSelected)
    view.onClickRefinement(::onSelectedRefinement.get())
}

fun <T> HierarchicalRefinementModel<T>.connectViews(views: List<HierarchicalView<T>>) {
    views.forEach(::connectView)
}