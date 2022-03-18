package com.algolia.instantsearch.android.hits

import android.os.Build
import android.widget.AutoCompleteTextView
import androidx.annotation.RequiresApi
import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.android.hits.internal.HitsArrayAdapterConnection

@RequiresApi(Build.VERSION_CODES.Q)
public fun <R, T> Searcher<R>.connectHitsArrayAdapter(
    adapter: HitsArrayAdapter<T>,
    view: AutoCompleteTextView,
    presenter: Presenter<R, List<T>>,
): Connection {
    return HitsArrayAdapterConnection(this, adapter, view, presenter)
}
