package com.algolia.instantsearch.demo.list.nested

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.search.model.IndexName
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.search.Query
import kotlinx.android.synthetic.main.demo_search.*
import kotlinx.android.synthetic.main.include_search.*


class NestedListDemo : AppCompatActivity() {

    private val searcher = SearcherMultipleIndex(
        client, listOf(
            IndexQuery(IndexName("mobile_demo_movies"), Query()),
            IndexQuery(IndexName("mobile_demo_actors"), Query())
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_search)

        val adapter = NestedListAdapter(searcher, this)

        configureToolbar(toolbar)
        configureSearchBox(searchView, searcher)
        configureSearchView(searchView, getString(R.string.search_movies))
        configureRecyclerView(list, adapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}