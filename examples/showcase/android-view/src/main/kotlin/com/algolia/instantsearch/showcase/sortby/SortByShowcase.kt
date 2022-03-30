package com.algolia.instantsearch.showcase.sortby

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.android.sortby.SortByViewAutocomplete
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.sortby.SortByConnector
import com.algolia.instantsearch.sortby.connectView
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.client
import com.algolia.instantsearch.showcase.configureRecyclerView
import com.algolia.instantsearch.showcase.configureToolbar
import com.algolia.instantsearch.showcase.databinding.ShowcaseSortByBinding
import com.algolia.instantsearch.showcase.list.movie.Movie
import com.algolia.instantsearch.showcase.list.movie.MovieAdapter
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName

class SortByShowcase : AppCompatActivity() {

    private val indexTitle = IndexName("mobile_demo_movies")
    private val indexYearAsc = IndexName("mobile_demo_movies_year_asc")
    private val indexYearDesc = IndexName("mobile_demo_movies_year_desc")
    private val searcher = HitsSearcher(client, indexTitle)
    private val indexes = mapOf(
        0 to indexTitle,
        1 to indexYearAsc,
        2 to indexYearDesc
    )
    private val sortBy = SortByConnector(searcher, indexes, selected = 0)
    private val connection = ConnectionHandler(sortBy)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseSortByBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter<String>(this, R.layout.menu_item)
        val view = SortByViewAutocomplete(binding.autoCompleteTextView, adapter)
        val adapterMovie = MovieAdapter()

        connection += sortBy.connectView(view) { indexName ->
            when (indexName) {
                indexTitle -> "Default"
                indexYearAsc -> "Year Asc"
                indexYearDesc -> "Year Desc"
                else -> indexName.raw
            }
        }
        connection += searcher.connectHitsView(adapterMovie) { response ->
            response.hits.deserialize(Movie.serializer())
        }

        configureToolbar(binding.toolbar)
        configureRecyclerView(binding.hits, adapterMovie)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
