package com.algolia.instantsearch.examples.android.guides.querysuggestion

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.examples.android.R
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

    private fun showSuggestions() {
        supportFragmentManager.commit {
            replace<SuggestionFragment>(R.id.container)
            setReorderingAllowed(true)
            addToBackStack("suggestions") // name can be null
        }
    }

    private fun showProducts() {
        supportFragmentManager.commit {
            replace<ProductFragment>(R.id.container)
            setReorderingAllowed(true)
            addToBackStack("products") // name can be null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connection.clear()
    }
}
