package com.algolia.instantsearch.guides.gettingstarted

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.filter.facet.FacetListAdapter
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.filter.facet.connectView
import com.algolia.instantsearch.guides.R
import com.algolia.instantsearch.guides.extension.configure

class FacetFragment : Fragment(R.layout.fragment_facet) {

    private val viewModel: MyViewModel by activityViewModels()
    private val connection = ConnectionHandler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(view)

        val adapterFacet = FacetListAdapter(MyFacetListViewHolder.Factory)
        val facetList = view.findViewById<RecyclerView>(R.id.facetList)
        connection += viewModel.facetList.connectView(adapterFacet, viewModel.facetPresenter)

        facetList.configure(adapterFacet)
    }

    private fun setupToolbar(view: View) {
        (requireActivity() as AppCompatActivity).let {
            it.setSupportActionBar(view.findViewById(R.id.toolbar))
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connection.clear()
    }
}
