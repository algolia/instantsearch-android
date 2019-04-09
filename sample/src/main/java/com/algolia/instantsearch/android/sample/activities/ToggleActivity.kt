package com.algolia.instantsearch.android.sample.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.android.Stats
import com.algolia.instantsearch.android.sample.R
import com.algolia.instantsearch.android.sample.views.OneValueToggle
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet
import kotlinx.android.synthetic.main.activity_toggle.*
import refinement.*
import refinement.RefinementListViewModel.Mode.SingleChoice

class ToggleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toggle)
        val searcher = initSearcherSingleIndex()
        searcher.responseListeners += {
            toast("Response!")
        }
        searcher.errorListeners += {
            toast("Error: $it")
        }
        // Prepare widgets
        val toggle = OneValueToggle(checkedTextView, Attribute("brand"), "Chanel")
        val stats = Stats(statsView)
        stats.connectWithSearcher(searcher)

        val state = SearchFilterState()
        val model = RefinementListViewModel<Facet>(SingleChoice) // Sets selection mode for presentation
        // Links view <> RefinementModel listeners, to keep display in sync with model
        model.connectView(toggle)

        // Links model to state, updating state on refinement and presenting on new state
        state.connectRefinementModel(model, toggle.attribute, Operator.AND)

        // Links model to searcher, updating model -> view on new results
        model.connectSearcherSingleIndex(searcher, toggle.attribute)
        state.connectSearcherSingleIndex(searcher) // Links state to searcher, querying on new state

        // Trigger search
        searcher.search()
    }
}
