package com.algolia.instantsearch.showcase.compose.relateditems

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.relateditems.MatchingPattern
import com.algolia.instantsearch.relateditems.connectRelatedHitsView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.compose.client
import com.algolia.instantsearch.showcase.compose.configureSearcher
import com.algolia.instantsearch.showcase.compose.model.Product
import com.algolia.instantsearch.showcase.compose.showcaseTitle
import com.algolia.instantsearch.showcase.compose.stubIndexName
import com.algolia.instantsearch.showcase.compose.ui.GreyLight
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.ProductList
import com.algolia.instantsearch.showcase.compose.ui.component.TitleTopBar
import com.algolia.search.helper.deserialize
import com.algolia.search.model.Attribute

class RelatedItemsShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val relatedItemsSearcher = HitsSearcher(client, stubIndexName)

    private val productsHits = HitsState<Product>()
    private val relatedItems = HitsState<Product>()

    private val matchingPatterns: List<MatchingPattern<Product>> = listOf(
        MatchingPattern(Attribute("brand"), 1, Product::brand),
        MatchingPattern(Attribute("categories"), 2, Product::categories)
    )

    private var relatedItemConnection: Connection? = null
    private val productsSearchConnection = searcher
        .connectHitsView(productsHits) { it.hits.deserialize(Product.serializer()) }
        .also { it.connect() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                RelatedItemsScreen(
                    hits = productsHits.hits,
                    relatedItems = relatedItems.hits,
                    onProductClick = ::relatedItemsOf
                )
            }
        }
        searcher.query.hitsPerPage = 3 // Limit to 3 results
        configureSearcher(searcher)
        configureSearcher(relatedItemsSearcher)
        searcher.searchAsync()
    }

    @Composable
    fun RelatedItemsScreen(
        title: String = showcaseTitle,
        hits: List<Product>,
        relatedItems: List<Product>,
        onProductClick: (Product) -> Unit,
    ) {
        Scaffold(
            topBar = {
                TitleTopBar(
                    title = title,
                    onBackPressed = ::onBackPressed,
                )
            },
            content = {
                Column(Modifier.fillMaxWidth()) {
                    ProductList(products = hits, onItemClick = onProductClick)
                    Text(
                        text = "Related Items",
                        style = MaterialTheme.typography.subtitle2,
                        color = GreyLight,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    ProductList(products = relatedItems)
                }
            }
        )
    }

    private fun relatedItemsOf(product: Product) {
        relatedItemConnection?.disconnect()
        relatedItemConnection = relatedItemsSearcher.connectRelatedHitsView(
            relatedItems, product, matchingPatterns
        ) { response ->
            response.hits.deserialize(Product.serializer())
        }
        relatedItemConnection?.connect()
        relatedItemsSearcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        relatedItemsSearcher.cancel()
        relatedItemConnection?.disconnect()
        productsSearchConnection.disconnect()
    }
}
