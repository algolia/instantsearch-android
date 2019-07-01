package com.algolia.instantsearch.demo.highlighting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.demo.list.movie.Movie
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.helper.deserialize
import kotlinx.android.synthetic.main.demo_search.*
import kotlinx.android.synthetic.main.include_search.*


class HighlightingDemo : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_highlighting)

        val adapter = MovieAdapterHighlighted()

        searcher.onResponseChanged += {
            adapter.submitList(it.hits.deserialize(Movie.serializer()))
        }

        configureToolbar(toolbar)
        configureSearcher(searcher)
        configureRecyclerView(list, adapter)
        configureSearchView(searchView, getString(R.string.search_movies))
        configureSearchBox(searchView, searcher)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}
