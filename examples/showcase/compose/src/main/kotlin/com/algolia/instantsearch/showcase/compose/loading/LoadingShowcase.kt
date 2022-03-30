package com.algolia.instantsearch.showcase.compose.loading

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.android.paging3.flow
import com.algolia.instantsearch.android.paging3.searchbox.connectPaginator
import com.algolia.instantsearch.compose.loading.LoadingState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.loading.LoadingConnector
import com.algolia.instantsearch.loading.connectView
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.compose.client
import com.algolia.instantsearch.showcase.compose.configureSearcher
import com.algolia.instantsearch.showcase.compose.model.Movie
import com.algolia.instantsearch.showcase.compose.stubIndexName
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.MoviesList
import com.algolia.instantsearch.showcase.compose.ui.component.SearchTopBar
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.Flow

class LoadingShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val paginator = Paginator(searcher) { it.deserialize(Movie.serializer()) }

    private val loadingState = LoadingState()
    private val loading = LoadingConnector(searcher)

    private val searchBoxState = SearchBoxState()
    private val searchBox = SearchBoxConnector(searcher)

    private val connections = ConnectionHandler(loading, searchBox)

    init {
        connections += loading.connectView(loadingState)
        connections += searchBox.connectView(searchBoxState)
        connections += searchBox.connectPaginator(paginator)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                LoadingScreen(
                    loadingState = loadingState,
                    searchBoxState = searchBoxState,
                    moviesFlow = paginator.flow
                )
            }
        }
        configureSearcher(searcher)
        searcher.searchAsync()
    }

    @Composable
    fun LoadingScreen(
        loadingState: LoadingState,
        searchBoxState: SearchBoxState,
        moviesFlow: Flow<PagingData<Movie>>
    ) {
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
                SwipeRefresh(
                    state = rememberSwipeRefreshState(loadingState.loading),
                    onRefresh = { loadingState.reload() },
                ) {
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
