package com.algolia.instantsearch.demo.list.paging

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.demo.list.Movie
import com.algolia.instantsearch.demo.list.MovieAdapter
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import kotlinx.android.synthetic.main.demo_paging.*


class PagingSingleIndexDemo : DemoActivity() {

    private val dataSourceFactory = SearcherSingleIndexDataSource.Factory(searcher, Movie.serializer())
    private val pagedListConfig = PagedList.Config.Builder().setPageSize(10).build()
    private val movies = LivePagedListBuilder<Int, Movie>(dataSourceFactory, pagedListConfig).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_paging)

        val movieAdapter = MovieAdapter()
        movies.observe(this, Observer { hits -> movieAdapter.submitList(hits) })
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searcher.setQuery(newText)
                movies.value?.dataSource?.let {
                    it.addInvalidatedCallback { list.scrollToPosition(0) }
                    it.invalidate()
                }
                return true
            }
        })

        configureSearchView(searchView)
        configureRecyclerView(list, movieAdapter)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}
