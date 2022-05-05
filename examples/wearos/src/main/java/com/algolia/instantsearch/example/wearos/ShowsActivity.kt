package com.algolia.instantsearch.example.wearos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.wear.widget.WearableLinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.search.helper.deserialize

class ShowsActivity : ComponentActivity() {

    private val showsViewModel: ShowsViewModel by viewModels()
    private val connections = ConnectionHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows)

        val showAdapter = ShowAdapter()
        connections += showsViewModel.searcher.connectHitsView(showAdapter) {
            it.hits.deserialize(Show.serializer())
        }

        findViewById<WearableRecyclerView>(R.id.list).apply {
            adapter = showAdapter
            layoutManager = WearableLinearLayoutManager(this@ShowsActivity, CustomScrollingLayoutCallback())
        }

        showsViewModel.search(query)
    }

    private val query: String
        get() = intent.getStringExtra(ExtraQuery) ?: ""

    override fun onDestroy() {
        super.onDestroy()
        connections.clear()
    }

    companion object {
        const val ExtraQuery = "query"
    }
}
