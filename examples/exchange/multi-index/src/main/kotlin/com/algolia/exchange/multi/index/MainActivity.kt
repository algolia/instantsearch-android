package com.algolia.exchange.multi.index

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SearchScreen(
                    searchBoxState = viewModel.searchBoxState,
                    actorsState = viewModel.actorsState,
                    moviesState = viewModel.moviesState,
                )
            }
        }
    }
}
