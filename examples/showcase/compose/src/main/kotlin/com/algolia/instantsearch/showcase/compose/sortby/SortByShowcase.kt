package com.algolia.instantsearch.showcase.compose.sortby

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.sortby.SortByState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.sortby.SortByConnector
import com.algolia.instantsearch.sortby.connectView
import com.algolia.instantsearch.showcase.compose.client
import com.algolia.instantsearch.showcase.compose.model.Movie
import com.algolia.instantsearch.showcase.compose.showcaseTitle
import com.algolia.instantsearch.showcase.compose.ui.component.DropdownTextField
import com.algolia.instantsearch.showcase.compose.ui.component.MoviesList
import com.algolia.instantsearch.showcase.compose.ui.component.TitleTopBar
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName

class SortByShowcase : AppCompatActivity() {

    private val movies = IndexName("mobile_demo_movies")
    private val moviesAsc = IndexName("mobile_demo_movies_year_asc")
    private val moviesDesc = IndexName("mobile_demo_movies_year_desc")
    private val hitsState = HitsState<Movie>()
    private val indexes = mapOf(
        0 to movies,
        1 to moviesAsc,
        2 to moviesDesc
    )
    private val searcher = HitsSearcher(client, movies)
    private val sortByState = SortByState()
    private val sortBy = SortByConnector(searcher, indexes, selected = 0)
    private val connections = ConnectionHandler(sortBy)

    init {
        connections += searcher.connectHitsView(hitsState) { it.hits.deserialize(Movie.serializer()) }
        connections += sortBy.connectView(sortByState) { indexName ->
            when (indexName) {
                movies -> "Default"
                moviesAsc -> "Year Asc"
                moviesDesc -> "Year Desc"
                else -> indexName.raw
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SortByScreen()
        }
        searcher.searchAsync()
    }

    @Composable
    fun SortByScreen(title: String = showcaseTitle) {
        Scaffold(
            topBar = {
                TitleTopBar(
                    title = title,
                    onBackPressed = ::onBackPressed
                )
            },
            content = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            modifier = Modifier.padding(12.dp),
                            text = "Sort by",
                            style = MaterialTheme.typography.subtitle2
                        )
                        DropdownTextField(
                            modifier = Modifier
                                .width(192.dp)
                                .padding(12.dp),
                            sortByState = sortByState
                        )
                    }
                    MoviesList(
                        modifier = Modifier.fillMaxWidth(),
                        movies = hitsState.hits
                    )
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connections.clear()
    }
}
