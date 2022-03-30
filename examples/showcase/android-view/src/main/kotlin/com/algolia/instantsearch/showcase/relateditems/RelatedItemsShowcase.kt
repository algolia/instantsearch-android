package com.algolia.instantsearch.showcase.relateditems

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.relateditems.MatchingPattern
import com.algolia.instantsearch.relateditems.connectRelatedHitsView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.databinding.ShowcaseRelateditemsBinding
import com.algolia.instantsearch.showcase.list.product.Product
import com.algolia.instantsearch.showcase.list.product.ProductAdapter
import com.algolia.search.helper.deserialize
import com.algolia.search.model.Attribute

class RelatedItemsShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val relatedItemsSearcher = HitsSearcher(client, stubIndexName)
    private val connection = ConnectionHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseRelateditemsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        searcher.query.hitsPerPage = 3 // Limit to 3 results

        configureToolbar(binding.toolbar)
        configureSearcher(searcher)
        configureSearcher(relatedItemsSearcher)

        val hitsAdapter = ProductAdapter()
        configureRecyclerView(binding.hits, hitsAdapter)
        connection += searcher.connectHitsView(hitsAdapter) { response ->
            response.hits.deserialize(Product.serializer())
        }

        val relatedItemsAdapter = ProductAdapter()
        configureRecyclerView(binding.relatedItems, relatedItemsAdapter)
        val matchingPatterns: List<MatchingPattern<Product>> = listOf(
            MatchingPattern(Attribute("brand"), 1, Product::brand),
            MatchingPattern(Attribute("categories"), 2, Product::categories)
        )
        hitsAdapter.callback = { product ->
            connection += relatedItemsSearcher.connectRelatedHitsView(relatedItemsAdapter, product, matchingPatterns) { response ->
                response.hits.deserialize(Product.serializer())
            }
            relatedItemsSearcher.searchAsync()
        }

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        relatedItemsSearcher.cancel()
        connection.clear()
    }
}
