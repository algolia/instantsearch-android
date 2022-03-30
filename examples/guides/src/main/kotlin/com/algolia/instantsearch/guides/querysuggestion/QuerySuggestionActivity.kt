package com.algolia.instantsearch.guides.querysuggestion

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.guides.R
import com.algolia.instantsearch.guides.querysuggestion.product.ProductFragment
import com.algolia.instantsearch.guides.querysuggestion.suggestion.SuggestionFragment
import com.algolia.instantsearch.searchbox.connectView

class QuerySuggestionActivity : AppCompatActivity() {

    private val viewModel by viewModels<QuerySuggestionViewModel>()
    private val connection = ConnectionHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_suggestion)

        // Setup search box
        val searchView = findViewById<SearchView>(R.id.searchView)
        val searchBoxView = SearchBoxViewAppCompat(searchView)
        connection += viewModel.searchBox.connectView(searchBoxView)

        // Switch fragments on search box focus
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) showSuggestions() else showProducts()
        }

        // Observe suggestions
        viewModel.suggestions.observe(this) { searchBoxView.setText(it.query, true) }

        // Initially show products view
        showProducts()
    }

    /** display suggestions fragment */
    private fun showSuggestions() {
        supportFragmentManager.commit {
            replace<SuggestionFragment>(R.id.container)
            setReorderingAllowed(true)
        }
    }

    /** display products fragment */
    private fun showProducts() {
        supportFragmentManager.commit {
            replace<ProductFragment>(R.id.container)
            setReorderingAllowed(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connection.clear()
    }
}
