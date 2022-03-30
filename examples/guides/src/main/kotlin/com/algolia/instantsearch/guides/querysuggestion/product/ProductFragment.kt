package com.algolia.instantsearch.guides.querysuggestion.product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.guides.R
import com.algolia.instantsearch.guides.extension.configure
import com.algolia.instantsearch.guides.querysuggestion.QuerySuggestionViewModel
import com.algolia.search.helper.deserialize

class ProductFragment : Fragment(R.layout.fragment_items) {

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

        // Run initial search
        viewModel.productSearcher.searchAsync()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connection.clear()
    }
}
