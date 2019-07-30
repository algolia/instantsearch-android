package com.algolia.instantsearch.demo.list.merged

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.searcher.connectView
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.demo.list.actor.Actor
import com.algolia.instantsearch.demo.list.movie.Movie
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.search.Query
import kotlinx.android.synthetic.main.demo_search.*
import kotlinx.android.synthetic.main.include_search.*


class MergedListDemo : AppCompatActivity() {

    private val searcher = SearcherMultipleIndex(
        client,
        listOf(
            IndexQuery(IndexName("mobile_demo_movies"), Query(hitsPerPage = 3)),
            IndexQuery(IndexName("mobile_demo_actors"), Query(hitsPerPage = 5))
        )
    )
    private val searchBox = SearchBoxConnector(searcher)
    private val connection = ConnectionHandler(searchBox)
    private val adapter = MergedListAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_search)

        val searchBoxView = SearchBoxViewAppCompat(searchView)

        connection += searcher.connectView(adapter::submitList) { response ->
            if (response != null) {
                mutableListOf<Any>().apply {
                    add("Actors")
                    addAll(response.results[1].hits.deserialize(Actor.serializer()))
                    add("Movies")
                    addAll(response.results[0].hits.deserialize(Movie.serializer()))
                }
            } else emptyList()
        }
        connection += searchBox.connectView(searchBoxView)

        configureToolbar(toolbar)
        configureSearchView(searchView, getString(R.string.search_movies))
        configureRecyclerView(list, adapter)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.disconnect()
    }
}