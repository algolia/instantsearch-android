package com.algolia.instantsearch.showcase.compose.filter.facet

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.filter.facet.FacetListState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.filter.facet.FacetListConnector
import com.algolia.instantsearch.filter.facet.FacetListPresenterImpl
import com.algolia.instantsearch.filter.facet.FacetSortCriterion
import com.algolia.instantsearch.filter.facet.connectView
import com.algolia.instantsearch.filter.state.filterState
import com.algolia.instantsearch.filter.state.groupAnd
import com.algolia.instantsearch.filter.state.groupOr
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.compose.*
import com.algolia.instantsearch.showcase.compose.ui.HoloBlueDark
import com.algolia.instantsearch.showcase.compose.ui.HoloGreenDark
import com.algolia.instantsearch.showcase.compose.ui.HoloRedDark
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.FacetList
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilter
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilterConnector
import com.algolia.instantsearch.showcase.compose.ui.component.TitleTopBar
import com.algolia.search.model.Attribute

class FacetListShowcase : AppCompatActivity() {

    private val color = Attribute("color")
    private val promotions = Attribute("promotions")
    private val category = Attribute("category")
    private val groupColor = groupAnd(color)
    private val groupPromotions = groupAnd(promotions)
    private val groupCategory = groupOr(category)
    private val filterState = filterState { group(groupColor) { facet(color, "green") } }
    private val searcher = HitsSearcher(client, stubIndexName)

    private val facetListStateColor = FacetListState()
    private val colorPresenter = FacetListPresenterImpl(
        listOf(
            FacetSortCriterion.IsRefined,
            FacetSortCriterion.AlphabeticalAscending
        ), limit = 3
    )
    private val facetListColor = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = color,
        selectionMode = SelectionMode.Single,
        groupID = groupColor
    )

    private val facetListStatePromotions = FacetListState()
    private val promotionPresenter =
        FacetListPresenterImpl(listOf(FacetSortCriterion.CountDescending))
    private val facetListPromotions = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = promotions,
        selectionMode = SelectionMode.Multiple,
        groupID = groupPromotions
    )

    private val facetListStateCategory = FacetListState()
    private val categoryPresenter = FacetListPresenterImpl(
        listOf(
            FacetSortCriterion.CountDescending,
            FacetSortCriterion.AlphabeticalAscending
        )
    )
    private val facetListCategory = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = category,
        selectionMode = SelectionMode.Multiple,
        groupID = groupCategory
    )

    private val filterHeader = HeaderFilterConnector(
        searcher = searcher,
        filterState = filterState,
        filterColors = filterColors(color, promotions, category)
    )

    private val connections = ConnectionHandler(
        facetListColor, facetListPromotions, facetListCategory, filterHeader
    )

    init {
        connections += searcher.connectFilterState(filterState)
        connections += facetListColor.connectView(facetListStateColor)
        connections += facetListCategory.connectView(facetListStateCategory)
        connections += facetListPromotions.connectView(facetListStatePromotions)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                FilterListScreen()
            }
        }

        configureSearcher(searcher)
        searcher.searchAsync()
    }

    @Composable
    fun FilterListScreen(title: String = showcaseTitle) {
        Scaffold(
            topBar = {
                TitleTopBar(
                    title = title,
                    onBackPressed = ::onBackPressed
                )
            },
            content = {
                Column(Modifier.fillMaxWidth()) {
                    HeaderFilter(
                        modifier = Modifier.padding(16.dp),
                        filterHeader = filterHeader
                    )
                    Row(Modifier.padding(horizontal = 16.dp)) {
                        Column(Modifier.weight(0.5f)) {
                            FacetList(
                                titleColor = HoloRedDark,
                                presenter = colorPresenter,
                                filterGroupID = groupColor,
                                facetListState = facetListStateColor
                            )
                            FacetList(
                                titleColor = HoloGreenDark,
                                presenter = promotionPresenter,
                                filterGroupID = groupPromotions,
                                facetListState = facetListStatePromotions
                            )
                        }

                        Column(Modifier.weight(0.5f)) {
                            FacetList(
                                titleColor = HoloBlueDark,
                                presenter = categoryPresenter,
                                filterGroupID = groupCategory,
                                facetListState = facetListStateCategory
                            )
                        }
                    }
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connections.clear()
    }
}
