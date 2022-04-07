package com.algolia.instantsearch.examples.showcase.view.list.paging

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingConfig
import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.android.paging3.liveData
import com.algolia.instantsearch.android.paging3.searchbox.connectPaginator
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.examples.R
import com.algolia.instantsearch.examples.databinding.IncludeSearchBinding
import com.algolia.instantsearch.examples.databinding.ShowcasePagingBinding
import com.algolia.instantsearch.examples.showcase.shared.model.Movie
import com.algolia.instantsearch.examples.showcase.view.client
import com.algolia.instantsearch.examples.showcase.view.configureRecyclerView
import com.algolia.instantsearch.examples.showcase.view.configureSearchView
import com.algolia.instantsearch.examples.showcase.view.configureSearcher
import com.algolia.instantsearch.examples.showcase.view.configureToolbar
import com.algolia.instantsearch.examples.showcase.view.list.movie.MovieAdapterPaged
import com.algolia.instantsearch.examples.showcase.view.onResponseChangedThenUpdateNbHits
import com.algolia.instantsearch.examples.showcase.view.stubIndexName
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.HitsSearcher

class PagingSingleIndexShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val paginator = Paginator(
        searcher = searcher,
        pagingConfig = PagingConfig(pageSize = 10, enablePlaceholders = false)
    ) { it.deserialize(Movie.serializer()) }
    private val searchBox = SearchBoxConnector(searcher)
    private val connection = ConnectionHandler(searchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcasePagingBinding.inflate(layoutInflater)
        val searchBinding = IncludeSearchBinding.bind(binding.searchBox.root)
        setContentView(binding.root)

        val adapter = MovieAdapterPaged()
        val searchBoxView = SearchBoxViewAppCompat(searchBinding.searchView)

        connection += searchBox.connectView(searchBoxView)
        connection += searchBox.connectPaginator(paginator)

        paginator.liveData.observe(this) { adapter.submitData(lifecycle, it) }

        configureToolbar(binding.toolbar)
        configureSearcher(searcher)
        configureSearchView(searchBinding.searchView, getString(R.string.search_movies))
        configureRecyclerView(binding.hits, adapter)
        onResponseChangedThenUpdateNbHits(searcher, binding.nbHits, connection)
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
