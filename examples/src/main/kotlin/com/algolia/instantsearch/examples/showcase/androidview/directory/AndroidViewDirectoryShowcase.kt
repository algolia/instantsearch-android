package com.algolia.instantsearch.examples.showcase.androidview.directory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.examples.R
import com.algolia.instantsearch.examples.databinding.IncludeSearchBinding
import com.algolia.instantsearch.examples.databinding.ShowcaseDirectoryBinding
import com.algolia.instantsearch.examples.directory.DirectoryAdapter
import com.algolia.instantsearch.examples.directory.directoryItems
import com.algolia.instantsearch.examples.showcase.androidview.client
import com.algolia.instantsearch.examples.showcase.androidview.configureRecyclerView
import com.algolia.instantsearch.examples.showcase.androidview.configureSearchBox
import com.algolia.instantsearch.examples.showcase.androidview.configureSearchView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query

class AndroidViewDirectoryShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(
        client = client,
        indexName = IndexName("mobile_demo_home"),
        query = Query(hitsPerPage = 100)
    )
    private val connection = ConnectionHandler()
    private val adapter = DirectoryAdapter()

    private lateinit var binding: ShowcaseDirectoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ShowcaseDirectoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connection += searcher.connectHitsView(adapter) { directoryItems(it, showcases) }

        val searchBinding = IncludeSearchBinding.bind(binding.searchBox.root)
        configureRecyclerView(binding.hits, adapter)
        configureSearchView(searchBinding.searchView, getString(R.string.search_showcases))
        configureSearchBox(searchBinding.searchView, searcher, connection)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
