package com.algolia.instantsearch.guides.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.algolia.instantsearch.guides.compose.theme.SearchAppTheme

class ComposeActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchAppTheme {
                Search(
                    searchBoxState = viewModel.searchBoxState,
                    paginator = viewModel.hitsPaginator,
                    statsText = viewModel.statsText,
                    facetList = viewModel.facetList
                )
            }
        }
    }
}
