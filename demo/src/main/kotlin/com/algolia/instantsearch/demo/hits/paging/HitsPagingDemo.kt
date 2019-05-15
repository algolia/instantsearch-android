package com.algolia.instantsearch.demo.hits.paging

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.demo.hits.HitsAdapter
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.IndexName
import com.algolia.search.model.response.ResponseSearch
import kotlinx.android.synthetic.main.demo_paging.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext


class HitsPagingDemo : AppCompatActivity(), CoroutineScope {

    private val index = client.initIndex(IndexName("mobile_demo_hits_paging"))
    private val searcher = SearcherSingleIndex(index)
    private val dataSourceFactory = HitsDataSourceFactory(this, searcher)
    private val config = PagedList.Config.Builder()
        .setPageSize(20)
        .build()

    override val coroutineContext: CoroutineContext = SupervisorJob()
    private val pagedHits = LivePagedListBuilder<Int, ResponseSearch.Hit>(dataSourceFactory, config).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_paging)

        val hitsAdapter = HitsAdapter()

        pagedHits.observeNotNull(this) {
            println(it)
            hitsAdapter.submitList(it)
        }

        searcher.connectSearchView(searchView, hitsAdapter)
        configureSearchView(searchView)

        configureRecyclerView(list, hitsAdapter)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)

        searcher.errorListeners += { throwable ->
            throwable.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }
}
