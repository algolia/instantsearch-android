package com.algolia.instantsearch.demo.guide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.connectView
import kotlinx.android.synthetic.main.product_fragment.*


class ProductFragment : Fragment() {

    private val connection = ConnectionHandler()
    private val adapter = ProductAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.product_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(requireActivity())[SampleViewModel::class.java]
        val searchBoxView = SearchBoxViewAppCompat(searchView)

        viewModel.products.observe(this, Observer { hits -> adapter.setHits(hits) })

        connection += viewModel.searchBox.connectView(searchBoxView)

        productList.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
            it.autoScrollToStart(adapter)
        }
        filters.setOnClickListener {
            println("here")
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .add(R.id.sampleDefaultFragment, RefinementFragment())
                .commit()
        }
    }

    fun RecyclerView.autoScrollToStart(adapter: RecyclerView.Adapter<*>) {
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    scrollToPosition(0)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connection.disconnect()
    }
}