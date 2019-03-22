package com.algolia.instantsearch.android.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.android.Stats
import com.algolia.instantsearch.android.sample.views.Hits
import com.algolia.instantsearch.android.sample.views.SearchBox
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query
import kotlinx.android.synthetic.main.activity_main.*
import searcher.SearcherSingleIndex


class MainActivity : AppCompatActivity() {
    //TODO: SingleIndexActivity, MultiIndexActivity, RefinementActivity
    //TODO: When moving to RefinementActivity, don't trigger a new request to display results

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup algolia
        val searcher = SearcherSingleIndex(
            ClientSearch(
                ApplicationID("latency"),
                APIKey("3cfd1f8bfa88c7709f6bacf8203194e8")
            ).initIndex(IndexName("products_android_demo")), Query()
        )

        // Prepare widgets
        val hitsWidget = Hits(hitsView)
        val stats = Stats(statsView)
        val searchBox = SearchBox.Support(searchView) {
            searcher.query.query = it
            searcher.search()
        }

        // Setup and trigger search
        searcher.responseListeners += {
            stats.updateView(it)
            hitsWidget.updateHits(it.hits)
        }
        searcher.search()
    }
}