package com.algolia.instantsearch.android.sample.activities

import android.content.Context
import android.widget.Toast
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query
import searcher.SearcherSingleIndex


internal fun initSearcherSingleIndex(): SearcherSingleIndex {
    return SearcherSingleIndex(
        ClientSearch(
            ApplicationID("latency"),
            APIKey("3cfd1f8bfa88c7709f6bacf8203194e8")
        ).initIndex(IndexName("products_android_demo")), Query()
    )
}

fun Context.toast(text: String, length: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, text, length).show()