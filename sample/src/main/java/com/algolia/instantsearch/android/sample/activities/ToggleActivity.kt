package com.algolia.instantsearch.android.sample.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.android.Stats
import com.algolia.instantsearch.android.sample.R
import com.algolia.instantsearch.android.sample.views.OneValueToggle
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet
import kotlinx.android.synthetic.main.activity_toggle.*
import refinement.RefinementListViewModel
import refinement.connectSearcherSingleIndex
import refinement.connectView

class ToggleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toggle)
        val searcher = initSearcherSingleIndex()

        // Prepare widgets
        val toggle = OneValueToggle(checkedTextView, Attribute("brand"), "Chanel")
        val stats = Stats(statsView)
        stats.connectWithSearcher(searcher)

        val model = RefinementListViewModel<Facet>(RefinementListViewModel.Mode.SingleChoice)
        model.connectSearcherSingleIndex(searcher, Attribute("brand"))
        model.connectView(toggle)

        // Trigger search
        searcher.search()
    }
}
