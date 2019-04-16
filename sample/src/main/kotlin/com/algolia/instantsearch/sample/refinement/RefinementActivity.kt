package com.algolia.instantsearch.sample.refinement

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.sample.R
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.dsl.facets
import com.algolia.search.dsl.query
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.FilterGroupsConverter
import filter.MutableFilterState
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
    private val query = query {
        facets {
            +color
            +promotion
            +category
        }
    }
    private val filterState = MutableFilterState()
    private val searcher = SearcherSingleIndex(index, query, filterState)

    override val coroutineContext = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.refinement_activity)


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
        val modelColors = RefinementFacetsViewModel(SelectionMode.SingleChoice)

        refinementWidget(
            view = listA as RecyclerView,
            title = listATitle,
            model = modelColors,
            presenter = RefinementFacetsPresenter(
                listOf(SortCriterion.IsRefined, SortCriterion.AlphabeticalAsc),
                limit = 5
            ),
            attribute = color,
            refinementMode = RefinementMode.And
        )

        refinementWidget(
            view = listB as RecyclerView,
            title = listBTitle,
            model = modelColors,
            presenter = RefinementFacetsPresenter(
                sortCriteria = listOf(SortCriterion.AlphabeticalDesc),
                limit = 3
            ),
            attribute = color,
            refinementMode = RefinementMode.And
        )

        refinementWidget(
            view = listC as RecyclerView,
            title = listCTitle,
            model = RefinementFacetsViewModel(SelectionMode.MultipleChoice),
            presenter = RefinementFacetsPresenter(
                sortCriteria = listOf(SortCriterion.CountDesc),
                limit = 5
            ),
            attribute = promotion,
            refinementMode = RefinementMode.And
        )

        refinementWidget(
            view = listD as RecyclerView,
            title = listDTitle,
            model = RefinementFacetsViewModel(SelectionMode.MultipleChoice),
            presenter = RefinementFacetsPresenter(
                sortCriteria = listOf(SortCriterion.CountDesc, SortCriterion.AlphabeticalAsc),
                limit = 5
            ),
            attribute = category,
            refinementMode = RefinementMode.Or
        )

        searcher.search()
    }

    private fun refinementWidget(
        view: RecyclerView,
        title: TextView,
        model: RefinementFacetsViewModel,
        presenter: RefinementFacetsPresenter,
        attribute: Attribute,
        refinementMode: RefinementMode
    ) {
        val adapter = RefinementAdapter()

        widget(attribute, searcher, model, presenter, adapter, refinementMode)

        view.layoutManager = LinearLayoutManager(this)
        view.adapter = adapter
        view.itemAnimator = null
        title.text = formatTitle(presenter, refinementMode)
    }
}