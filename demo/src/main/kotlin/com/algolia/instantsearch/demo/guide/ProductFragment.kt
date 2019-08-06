package com.algolia.instantsearch.demo.guide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.helper.android.list.autoScrollToStart
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.algolia.instantsearch.helper.android.stats.StatsTextView
import com.algolia.instantsearch.helper.stats.connectView
import com.algolia.instantsearch.helper.stats.StatsPresenterImpl
import kotlinx.android.synthetic.main.product_fragment.*


class ProductFragment : Fragment() {

    private val connection = ConnectionHandler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.product_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(requireActivity())[SampleViewModel::class.java]
        val searchBoxView = SearchBoxViewAppCompat(searchView)
        val statsView = StatsTextView(stats)

        viewModel.products.observe(this, Observer { hits -> viewModel.adapterProduct.setHits(hits) })

        connection += viewModel.searchBox.connectView(searchBoxView)
        connection += viewModel.stats.connectView(statsView, StatsPresenterImpl())

        productList.let {
            it.adapter = viewModel.adapterProduct
            it.layoutManager = LinearLayoutManager(requireContext())
            it.autoScrollToStart(viewModel.adapterProduct)
        }
        filters.setOnClickListener { (requireActivity() as SampleActivity).showFacetFragment() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connection.disconnect()
    }
}