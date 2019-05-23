package com.algolia.instantsearch.demo.list.paging

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.demo.list.Movie
import com.algolia.instantsearch.demo.list.MovieAdapterPaged
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.android.searchbox.connectSearchView
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import kotlinx.android.synthetic.main.demo_paging.*


class PagingSingleIndexDemo : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)
    private val dataSourceFactory = SearcherSingleIndexDataSource.Factory(searcher, Movie.serializer())
    private val pagedListConfig = PagedList.Config.Builder().setPageSize(10).build()
    private val movies = LivePagedListBuilder<Int, Movie>(dataSourceFactory, pagedListConfig).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_paging)

        val adapter = MovieAdapterPaged()
        val searchBoxViewModel = SearchBoxViewModel()

        searcher.index = client.initIndex(intent.indexName)
        movies.observe(this, Observer { hits -> adapter.submitList(hits) })

        searchBoxViewModel.connectSearchView(searchView)
        searchBoxViewModel.connectSearcher(searcher)
        searchBoxViewModel.onQueryChanged += {
            movies.value?.dataSource?.let { it.invalidate() }
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    list.scrollToPosition(0)
                }
            }
        })

        configureSearchView(searchView)
        configureRecyclerView(list, adapter)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}
