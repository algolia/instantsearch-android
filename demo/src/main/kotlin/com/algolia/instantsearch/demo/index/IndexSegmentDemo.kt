package com.algolia.instantsearch.demo.index

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.selectable.segment.SelectableSegmentView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.client
import com.algolia.instantsearch.demo.configureRecyclerView
import com.algolia.instantsearch.demo.configureToolbar
import com.algolia.instantsearch.demo.list.Movie
import com.algolia.instantsearch.demo.list.MovieAdapter
import com.algolia.instantsearch.helper.index.IndexSegmentViewModel
import com.algolia.instantsearch.helper.index.connectSearcher
import com.algolia.instantsearch.helper.index.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName
import kotlinx.android.synthetic.main.demo_index_segment.*


class IndexSegmentDemo : AppCompatActivity() {

    private val indexTitle = client.initIndex(IndexName("mobile_demo_movies"))
    private val indexYearAsc = client.initIndex(IndexName("mobile_demo_movies_year_asc"))
    private val indexYearDesc = client.initIndex(IndexName("mobile_demo_movies_year_desc"))

    private val searcher = SearcherSingleIndex(indexTitle)

    private val indexes = mapOf(
        0 to indexTitle,
        1 to indexYearAsc,
        2 to indexYearDesc
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_index_segment)

        val viewModel = IndexSegmentViewModel(indexes).apply {
            selected = 0
        }
        val adapter = ArrayAdapter<String>(this, R.layout.menu_item)
        val view = SelectableAutoCompleteTextView(autocompleteTextView, adapter)


        viewModel.connectSearcher(searcher)
        viewModel.connectView(view) { index ->
            when (index) {
                indexTitle -> "Default"
                indexYearAsc -> "Year Asc"
                indexYearDesc -> "Year Desc"
                else -> index.indexName.raw
            }
        }

        val adapterMovie = MovieAdapter()

        searcher.onResponseChanged += {
            adapterMovie.submitList(it.hits.deserialize(Movie.serializer()))
        }

        configureToolbar(toolbar)
        configureRecyclerView(list, adapterMovie)

        searcher.search()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}

public class SelectableAutoCompleteTextView(
    val view: AutoCompleteTextView,
    val adapter: ArrayAdapter<String>
) : SelectableSegmentView<Int, String>, AdapterView.OnItemClickListener {

    override var onClick: ((Int) -> Unit)? = null

    private var map: Map<Int, String>? = null

    init {
        view.setAdapter(adapter)
        view.onItemClickListener = this
    }

    override fun setSelected(selected: Int?) {
        map?.get(selected)?.let { view.setText(it, false) }
    }

    override fun setItems(items: Map<Int, String>) {
        map = items
        adapter.setNotifyOnChange(false)
        adapter.clear()
        adapter.addAll(items.values)
        adapter.notifyDataSetChanged()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onClick?.invoke(position)
    }
}