package com.algolia.instantsearch.guides.querysuggestion.suggestion

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.guides.R
import com.algolia.instantsearch.guides.querysuggestion.QuerySuggestionViewModel
import com.algolia.instantsearch.guides.extension.configure
import com.algolia.instantsearch.guides.model.Suggestion
import com.algolia.search.helper.deserialize

class SuggestionFragment : Fragment(R.layout.fragment_items) {

    private val viewModel: QuerySuggestionViewModel by activityViewModels()
    private val connection = ConnectionHandler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Configure suggestions view
        val suggestionAdapter = SuggestionAdapter {
            // On suggestion click, update the
            viewModel.suggestions.value = it
        }
        view.findViewById<RecyclerView>(R.id.items).configure(suggestionAdapter)
        connection += viewModel.suggestionSearcher.connectHitsView(suggestionAdapter) {
            it.hits.deserialize(Suggestion.serializer())
        }

        // Run initial search
        viewModel.suggestionSearcher.searchAsync()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connection.clear()
    }
}
