package com.algolia.instantsearch.showcase.directory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.databinding.IncludeSearchBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseDirectoryBinding
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query

class DirectoryShowcase : AppCompatActivity() {

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
        val searchBinding = IncludeSearchBinding.bind(binding.searchBox.root)


        connection += searcher.connectHitsView(adapter) { response ->
            response.hits.deserialize(DirectoryHit.serializer())
                .filter { showcases.containsKey(it.objectID) }
                .groupBy { it.type }
                .toSortedMap()
                .flatMap { (key, value) ->
                    listOf(DirectoryItem.Header(key)) + value.map { DirectoryItem.Item(it) }
                        .sortedBy { it.hit.objectID.raw }
                }
        }

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
