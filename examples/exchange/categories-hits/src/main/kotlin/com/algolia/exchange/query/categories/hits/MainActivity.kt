package com.algolia.exchange.query.categories.hits

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
                    hitsState = viewModel.hitsState,
                    categoriesState = viewModel.categoriesState,
                )
            }
        }
    }
}
