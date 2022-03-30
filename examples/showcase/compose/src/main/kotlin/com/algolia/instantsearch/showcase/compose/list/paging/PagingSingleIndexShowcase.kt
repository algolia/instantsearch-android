package com.algolia.instantsearch.showcase.compose.list.paging

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.android.paging3.flow
import com.algolia.instantsearch.android.paging3.searchbox.connectPaginator
import com.algolia.instantsearch.compose.item.StatsTextState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.compose.client
import com.algolia.instantsearch.showcase.compose.configureSearcher
import com.algolia.instantsearch.showcase.compose.model.Movie
import com.algolia.instantsearch.showcase.compose.stubIndexName
import com.algolia.instantsearch.showcase.compose.ui.GreyLight
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.MoviesList
import com.algolia.instantsearch.showcase.compose.ui.component.SearchTopBar
import com.algolia.instantsearch.stats.StatsConnector
import com.algolia.instantsearch.stats.StatsPresenterImpl
import com.algolia.instantsearch.stats.connectView
import kotlinx.coroutines.flow.Flow

class PagingSingleIndexShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val pagingConfig = PagingConfig(pageSize = 10)
    private val paginator = Paginator(searcher, pagingConfig) { it.deserialize(Movie.serializer()) }

    private val searchBoxState = SearchBoxState()
    private val searchBox = SearchBoxConnector(searcher)

    private val statsState = StatsTextState()
    private val stats = StatsConnector(searcher)

    private val connections = ConnectionHandler(searchBox, stats)

    init {
        connections += searchBox.connectView(searchBoxState)
        connections += searchBox.connectPaginator(paginator)
        connections += stats.connectView(statsState, StatsPresenterImpl())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                PagingSingleIndexScreen(
                    stats = statsState.stats,
                    moviesFlow = paginator.flow
                )
            }
        }
        configureSearcher(searcher)
    }

    @Composable
    fun PagingSingleIndexScreen(stats: String, moviesFlow: Flow<PagingData<Movie>>) {
        val moviesListState = rememberLazyListState()
        Scaffold(
            topBar = {
                SearchTopBar(
                    placeHolderText = "Search for movies",
                    searchBoxState = searchBoxState,
                    onBackPressed = ::onBackPressed,
                    lazyListState = moviesListState
                )
            },
            content = {
                Column(Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = stats,
                        style = MaterialTheme.typography.caption,
                        maxLines = 1,
                        color = GreyLight,
                    )
                    MoviesList(
                        movies = moviesFlow.collectAsLazyPagingItems(),
                        listState = moviesListState
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
