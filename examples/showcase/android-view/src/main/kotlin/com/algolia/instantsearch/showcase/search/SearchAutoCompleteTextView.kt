package com.algolia.instantsearch.showcase.search

import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.android.hits.HitsArrayAdapter
import com.algolia.instantsearch.android.hits.connectHitsArrayAdapter
import com.algolia.instantsearch.android.searchbox.SearchBoxAutoCompleteTextView
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.SearchMode
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.client
import com.algolia.instantsearch.showcase.configureSearcher
import com.algolia.instantsearch.showcase.databinding.ShowcaseSearchAutocompleteBinding
import com.algolia.instantsearch.showcase.list.movie.Movie
import com.algolia.instantsearch.showcase.stubIndexName
import com.algolia.search.helper.deserialize

class SearchAutoCompleteTextView : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val searchBox = SearchBoxConnector(searcher, searchMode = SearchMode.AsYouType)
    private val connection = ConnectionHandler(searchBox)

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseSearchAutocompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        val hitsAdapter = HitsArrayAdapter(adapter)
        val searchBoxView = SearchBoxAutoCompleteTextView(binding.autoCompleteTextView)

        binding.autoCompleteTextView.setAdapter(adapter)
        connection += searchBox.connectView(searchBoxView)
        connection += searcher.connectHitsArrayAdapter(
            hitsAdapter,
            binding.autoCompleteTextView
        ) { response ->
            response.hits.deserialize(Movie.serializer()).map { it.title }
        }

        configureSearcher(searcher)
        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
