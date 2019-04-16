package com.algolia.instantsearch.sample.refinement

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.sample.R
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.FilterGroupsConverter
import filter.toFilterGroups
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.android.synthetic.main.refinement_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.io.ByteReadChannel
import refinement.*
import search.SearcherSingleIndex


class RefinementActivity : AppCompatActivity(), CoroutineScope {

    private val mockEngine = MockEngine {
        MockHttpResponse(
            it.call,
            HttpStatusCode.OK,
            headers = headersOf("Content-Type", listOf(ContentType.Application.Json.toString())),
            content = ByteReadChannel(responseSerialized)
        )
    }
    private val client = ClientSearch(
        ConfigurationSearch(
            ApplicationID("mock"),
            APIKey("mock"),
            engine = mockEngine
        )
    )
    private val index = client.initIndex(IndexName("mock"))
    private val searcher = SearcherSingleIndex(index)

    override val coroutineContext = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.refinement_activity)

        val viewModelA = RefinementFacetsViewModel(SelectionMode.SingleChoice)
        val viewA = RefinementAdapter()
        val presenterA = RefinementFacetsPresenter(listOf(SortCriterion.IsRefined, SortCriterion.AlphabeticalAsc))

        viewModelA.connect(color, searcher, RefinementMode.And)
        viewModelA.connect(presenterA)
        viewModelA.connect(viewA)
        presenterA.connect(viewA)

        val viewB = RefinementAdapter()
        val presenterB = RefinementFacetsPresenter(listOf(SortCriterion.AlphabeticalDesc), 3)

        viewModelA.connect(color, searcher, RefinementMode.And)
        viewModelA.connect(presenterB)
        viewModelA.connect(viewB)
        presenterB.connect(viewB)

        val viewModelC = RefinementFacetsViewModel(SelectionMode.MultipleChoice)
        val viewC = RefinementAdapter()
        val presenterC = RefinementFacetsPresenter(listOf(SortCriterion.CountDesc))

        viewModelC.connect(promotion, searcher, RefinementMode.And)
        viewModelC.connect(presenterC)
        viewModelC.connect(viewC)
        presenterC.connect(viewC)

        val viewModelD = RefinementFacetsViewModel(SelectionMode.MultipleChoice)
        val viewD = RefinementAdapter()
        val presenterD = RefinementFacetsPresenter(listOf(SortCriterion.CountDesc, SortCriterion.AlphabeticalAsc))

        viewModelD.connect(category, searcher, RefinementMode.Or)
        viewModelD.connect(presenterD)
        viewModelD.connect(viewD)
        presenterD.connect(viewD)

        configureRecyclerView(listA, viewA)
        configureRecyclerView(listB, viewB)
        configureRecyclerView(listC, viewC)
        configureRecyclerView(listD, viewD)
        listATitle.text = formatTitle(presenterA, RefinementMode.And)
        listBTitle.text = formatTitle(presenterB, RefinementMode.And)
        listCTitle.text = formatTitle(presenterC, RefinementMode.And)
        listDTitle.text = formatTitle(presenterD, RefinementMode.Or)

        searcher.filterState.listeners += {
            val span = it.toFilterGroups().highlight(
                FilterGroupsConverter.SQL.Unquoted,
                listOf(
                    ContextCompat.getColor(this, android.R.color.holo_red_dark),
                    ContextCompat.getColor(this, android.R.color.holo_blue_dark),
                    ContextCompat.getColor(this, android.R.color.holo_green_dark)
                )
            )
            filtersTextView.text = span
        }
        searcher.search()
    }

    private fun configureRecyclerView(
        recyclerView: View,
        view: RefinementAdapter
    ) {
        (recyclerView as RecyclerView).let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = view
            it.itemAnimator = null
        }
    }
}