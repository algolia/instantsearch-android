package com.algolia.instantsearch.showcase.suggestion.suggestion

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.configure
import com.algolia.instantsearch.showcase.list.suggestion.Suggestion
import com.algolia.instantsearch.showcase.suggestion.QuerySuggestionViewModel
import com.algolia.search.helper.deserialize

class SuggestionFragment : Fragment(R.layout.list_items) {

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
        connection += viewModel.suggestionSearcher.connectHitsView(suggestionAdapter, past = true) {
            it.hits.deserialize(Suggestion.serializer())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connection.clear()
    }
}
