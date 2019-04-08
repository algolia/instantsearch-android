package com.algolia.instantsearch.android.sample.activities

import android.content.Context
import android.widget.Toast
import com.algolia.instantsearch.android.Stats
import com.algolia.instantsearch.android.sample.R.id.checkedTextView
import com.algolia.instantsearch.android.sample.R.id.statsView
import com.algolia.instantsearch.android.sample.views.OneValueToggle
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Facet
import com.algolia.search.model.search.Query
import refinement.*
import searcher.SearcherSingleIndex


internal fun initSearcherSingleIndex(): SearcherSingleIndex {
    return SearcherSingleIndex(
        ClientSearch(
            ApplicationID("latency"),
            APIKey("3cfd1f8bfa88c7709f6bacf8203194e8")
        ).initIndex(IndexName("products_android_demo")), Query()
    )
}

internal fun Context.addDebugListeners(searcher: SearcherSingleIndex) {
    searcher.responseListeners += {
        toast("Response")
    }
    searcher.errorListeners += {
        toast("Error: $it")
    }
}

internal fun prepareWidgets(searcher: SearcherSingleIndex, widgets: List<Any>) {
    val state = SearchFilterState()

    widgets.forEach {
        if (it is Stats) {
            it.connectWithSearcher(searcher)
        } else if (it is OneValueToggle) {
            // Links view <> RefinementModel listeners, to keep display in sync with model
            val model = RefinementListViewModel<Facet>(RefinementListViewModel.Mode.SingleChoice) // Sets selection mode for presentation
            model.connectView(it)

            // Links model to state, updating state on refinement and presenting on new state
            state.connectRefinementModel(model, it.attribute, Operator.AND)

            // Links model to searcher, updating model -> view on new results
            model.connectSearcherSingleIndex(searcher, it.attribute)
            state.connectSearcherSingleIndex(searcher) // Links state to searcher, querying on new state
        }
    }

}
fun Context.toast(text: String, length: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, text, length).show()