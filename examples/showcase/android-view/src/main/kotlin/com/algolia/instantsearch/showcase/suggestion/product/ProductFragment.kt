package com.algolia.instantsearch.showcase.suggestion.product

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.core.loading.LoadingView
import com.algolia.instantsearch.loading.connectView
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.configure
import com.algolia.instantsearch.showcase.suggestion.QuerySuggestionViewModel
import com.algolia.search.helper.deserialize

class ProductFragment : Fragment(R.layout.list_items) {

    private val viewModel: QuerySuggestionViewModel by activityViewModels()
    private val connection = ConnectionHandler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure products view
        val productAdapter = ProductAdapter()
        view.findViewById<RecyclerView>(R.id.items).configure(productAdapter)
        connection += viewModel.productSearcher.connectHitsView(productAdapter) {
            it.hits.deserialize(Product.serializer())
        }

        // Configure loading indicator
        val loadingView = loadingView(view.findViewById(R.id.progress))
        connection += viewModel.loading.connectView(loadingView)
    }

    private fun loadingView(progressBar: ProgressBar) = object : LoadingView {
        override var onReload: Callback<Unit>? = null
        override fun setIsLoading(isLoading: Boolean) {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connection.clear()
    }
}
