package com.algolia.instantsearch.android.sample.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.android.Stats
import com.algolia.instantsearch.android.sample.R
import com.algolia.instantsearch.android.sample.views.Hits
import com.algolia.instantsearch.android.sample.views.SearchBox
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //TODO: SingleIndexActivity, MultiIndexActivity, RefinementActivity
    //TODO: When moving to RefinementActivity, don't trigger a new request to display results

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup algolia
        val searcher = initSearcherSingleIndex()

        // Prepare widgets
        val hits = Hits(hitsView)
        val stats = Stats(statsView)
        val searchBox = SearchBox.Support(searchView) {
            searcher.query.query = it
            searcher.search()
        }
        stats.connectWithSearcher(searcher)
        hits.connectWithSearcher(searcher)

        // Trigger search
        searcher.search()
    }

}