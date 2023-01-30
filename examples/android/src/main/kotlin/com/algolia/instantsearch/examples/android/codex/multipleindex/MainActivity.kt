package com.algolia.instantsearch.examples.android.codex.multipleindex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.algolia.instantsearch.examples.android.AppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                SearchScreen(
                    searchBoxState = viewModel.searchBoxState,
                    actorsState = viewModel.actorsState,
                    moviesState = viewModel.moviesState,
                )
            }
        }
    }
}
