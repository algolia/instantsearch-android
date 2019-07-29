package com.algolia.instantsearch.demo.index

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.client
import com.algolia.instantsearch.demo.configureRecyclerView
import com.algolia.instantsearch.demo.configureToolbar
import com.algolia.instantsearch.demo.list.movie.Movie
import com.algolia.instantsearch.demo.list.movie.MovieAdapter
import com.algolia.instantsearch.helper.android.index.IndexSegmentViewAutocomplete
import com.algolia.instantsearch.helper.index.IndexSegmentWidget
import com.algolia.instantsearch.helper.index.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectListAdapter
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName
import kotlinx.android.synthetic.main.demo_index_segment.*


class IndexSegmentDemo : AppCompatActivity() {

    private val indexTitle = client.initIndex(IndexName("mobile_demo_movies"))
    private val indexYearAsc = client.initIndex(IndexName("mobile_demo_movies_year_asc"))
    private val indexYearDesc = client.initIndex(IndexName("mobile_demo_movies_year_desc"))
    private val searcher = SearcherSingleIndex(indexTitle)
    private val indexes = mapOf(
        0 to indexTitle,
        1 to indexYearAsc,
        2 to indexYearDesc
    )
    private val widgetIndexSegment = IndexSegmentWidget(indexes, searcher, selected = 0)
    private val connection = ConnectionHandler(widgetIndexSegment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_index_segment)

        val adapter = ArrayAdapter<String>(this, R.layout.menu_item)
        val view = IndexSegmentViewAutocomplete(autocompleteTextView, adapter)
        val adapterMovie = MovieAdapter()

        connection += widgetIndexSegment.connectView(view) { index ->
                when (index) {
                    indexTitle -> "Default"
                    indexYearAsc -> "Year Asc"
                    indexYearDesc -> "Year Desc"
                    else -> index.indexName.raw
                }
            }
        connection += searcher.connectListAdapter(adapterMovie) { hits -> hits.deserialize(Movie.serializer()) }

        configureToolbar(toolbar)
        configureRecyclerView(list, adapterMovie)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.disconnect()
    }
}