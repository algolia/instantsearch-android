package com.algolia.instantsearch.android.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.android.StatsWidget
import com.algolia.instantsearch.android.sample.views.Hits
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query
import kotlinx.android.synthetic.main.activity_main.*
import searcher.SearcherSingleIndex

<<<<<<< HEAD
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
<<<<<<< HEAD
import com.algolia.instantsearch.android.StatsWidget
=======
import com.algolia.instantsearch.android.Stats
import com.algolia.instantsearch.android.sample.views.Hits
import com.algolia.instantsearch.android.sample.views.SearchBox
>>>>>>> 67bf6b4... fixup! refactor(MainActivity): Extract HitsAdapter
=======
import com.algolia.instantsearch.android.Stats
>>>>>>> 5f05d38... refactor(StatsWidget): Rename to Stats
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
        val client = ClientSearch(ApplicationID("latency"), APIKey("3cfd1f8bfa88c7709f6bacf8203194e8"))
        val index = client.initIndex(IndexName("products_android_demo"))
        val searcher = SearcherSingleIndex(index, Query(""))

        // Prepare widgets
        val hitsWidget = Hits(hitsView)
        val stats = StatsWidget(statsView)
        val searchBox = SearchBox.Support(searchView)
        searchBox.onQueryTextChangeListener = {
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