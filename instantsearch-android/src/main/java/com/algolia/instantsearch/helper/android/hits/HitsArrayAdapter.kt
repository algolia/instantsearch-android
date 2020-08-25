package com.algolia.instantsearch.helper.android.hits

import android.widget.ArrayAdapter
import com.algolia.instantsearch.core.hits.HitsView


public class HitsArrayAdapter<T>(
   public val adapter: ArrayAdapter<T>
) : HitsView<T> {

    override fun setHits(hits: List<T>) {
        adapter.clear()
        adapter.addAll(hits)
    }
}