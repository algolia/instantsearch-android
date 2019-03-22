package com.algolia.instantsearch.android.sample.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.android.Stats
import com.algolia.instantsearch.android.sample.views.Hits
import com.algolia.instantsearch.android.sample.views.SearchBox
import kotlinx.android.synthetic.main.activity_main.*
import com.algolia.instantsearch.android.sample.R

class MainActivity : AppCompatActivity() {
    //TODO: SingleIndexActivity, MultiIndexActivity, RefinementActivity
    //TODO: When moving to RefinementActivity, don't trigger a new request to display results

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup algolia
        val searcher = initSearcherSingleIndex()

        // Prepare widgets
        val hitsWidget = Hits(hitsView)
        val stats = Stats(statsView)
        val searchBox = SearchBox.Support(searchView) {
            searcher.query.query = it
            searcher.search()
        }

//        val model = RefinementListViewModel<Facet>(RefinementListViewModel.Mode.SingleChoice)
//        model.connectSearcherSingleIndex(searcher, Attribute("brand"))

        // Setup and trigger search
        searcher.responseListeners += {
            stats.updateView(it)
            hitsWidget.updateHits(it.hits)
        }
        searcher.search()
    }

}