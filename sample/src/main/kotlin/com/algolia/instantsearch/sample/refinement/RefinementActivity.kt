package com.algolia.instantsearch.sample.refinement

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.sample.R
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.dsl.facets
import com.algolia.search.dsl.query
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.LogLevel
import kotlinx.android.synthetic.main.list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import refinement.RefinementFacetsPresenter
import refinement.RefinementFacetsViewModel
import refinement.SelectionMode
import refinement.connectWith
import search.SearcherSingleIndex


class RefinementActivity : AppCompatActivity(), CoroutineScope {

    private val client = ClientSearch(
        ConfigurationSearch(
            ApplicationID("latency"),
            APIKey("03cd233a16e1f5e874ddaff30504bb5a"),
            logLevel = LogLevel.ALL
        )
    )
    private val index = client.initIndex(IndexName("mobile_demo_refinement"))
    private val attribute = Attribute("color")
    private val query = query {
        facets {
            +attribute
        }
    }
    private val searcher = SearcherSingleIndex(index, query)
    private val model = RefinementFacetsViewModel(SelectionMode.MultipleChoice)
    private val presenter = RefinementFacetsPresenter()
    private val adapter = RefinementAdapter()

    override val coroutineContext = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.refinement_activity)

        model.connectWith(searcher, attribute)
        model.connectWith(presenter)
        model.connectWith(adapter)
        presenter.connectWith(adapter)

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter
        list.itemAnimator = null

        searcher.search()
    }
}