package com.algolia.instantsearch.examples.android.codex.categorieshits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import com.algolia.instantsearch.examples.android.AppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                SearchScreen(
                    searchBoxState = viewModel.searchBoxState,
                    productsState = viewModel.productsState,
                    categoriesState = viewModel.categoriesState,
                )
            }
        }
    }
}
