package com.algolia.instantsearch.android.hits.internal

import android.os.Build
import android.widget.AutoCompleteTextView
import androidx.annotation.RequiresApi
import com.algolia.instantsearch.android.hits.HitsArrayAdapter
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.searcher.Searcher

@RequiresApi(Build.VERSION_CODES.Q)
internal data class HitsArrayAdapterConnection<R, T>(
    private val searcher: Searcher<R>,
    private val adapter: HitsArrayAdapter<T>,
    private val view: AutoCompleteTextView,
    private val presenter: Presenter<R, List<T>>,
) : ConnectionImpl() {

    init {
        view.setAdapter(adapter.adapter)
    }

    private val callback: Callback<R?> = { response ->
        if (response != null) {
            adapter.adapter.apply {
                setNotifyOnChange(false)
                clear()
                addAll(presenter(response))
                notifyDataSetChanged()
            }
            view.refreshAutoCompleteResults()
        }
    }

    override fun connect() {
        super.connect()
        searcher.response.subscribe(callback)
    }

    override fun disconnect() {
        super.disconnect()
        searcher.response.unsubscribe(callback)
    }
}
