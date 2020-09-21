package com.algolia.instantsearch.helper.android.hits

import android.os.Build
import android.widget.AutoCompleteTextView
import androidx.annotation.RequiresApi
import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searcher.Searcher

@RequiresApi(Build.VERSION_CODES.Q)
public fun <R, T> Searcher<R>.connectHitsArrayAdapter(
    adapter: HitsArrayAdapter<T>,
    view: AutoCompleteTextView,
    presenter: Presenter<R, List<T>>,
): Connection {
    return HitsArrayAdapterConnection<R, T>(this, adapter, view, presenter)
}
