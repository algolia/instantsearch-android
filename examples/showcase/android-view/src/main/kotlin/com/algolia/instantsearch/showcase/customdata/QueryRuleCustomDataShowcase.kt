package com.algolia.instantsearch.showcase.customdata

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.customdata.QueryRuleCustomDataConnector
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.SearchMode
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.customdata.TemplateActivity.Companion.EXTRA_CONTENT
import com.algolia.instantsearch.showcase.databinding.IncludeSearchInfoBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseQueryRuleCustomDataBinding
import com.algolia.instantsearch.showcase.list.product.Product
import com.algolia.instantsearch.showcase.list.product.ProductAdapter
import com.algolia.search.helper.deserialize
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class QueryRuleCustomDataShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val searchBox = SearchBoxConnector(searcher, searchMode = SearchMode.AsYouType)
    private val queryRuleCustomData = QueryRuleCustomDataConnector<Banner>(searcher)
    private val connection = ConnectionHandler(searchBox, queryRuleCustomData)

    private lateinit var binding: ShowcaseQueryRuleCustomDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ShowcaseQueryRuleCustomDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val searchBinding = IncludeSearchInfoBinding.bind(binding.searchBox.root)

        val adapter = ProductAdapter()
        val searchBoxView = SearchBoxViewAppCompat(searchBinding.searchView)

        connection += searchBox.connectView(searchBoxView)
        connection += searcher.connectHitsView(adapter) { response ->
            response.hits.deserialize(Product.serializer())
        }

        queryRuleCustomData.subscribe { model ->
            when {
                model == null -> noBanner()
                model.banner != null -> showBannerImage(model)
                model.title != null -> showBannerText(model)
            }
        }

        searchBox.viewModel.eventSubmit.subscribe {
            val model = queryRuleCustomData.viewModel.item.value ?: return@subscribe
            if (model.banner == null && model.title == null) {
                redirect(model.link, resources.getString(R.string.redirect_via_submit))
            }
        }

        configureToolbar(binding.toolbar)
        configureSearcher(searcher)
        configureRecyclerView(binding.hits, adapter)
        configureSearchView(searchBinding.searchView, getString(R.string.search_products))
        configureHelp(searchBinding.info)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }

    private fun showBannerImage(model: Banner) {
        binding.bannerImage.visibility = View.VISIBLE
        Glide.with(this)
            .load(model.banner)
            .fitCenter()
            .into(binding.bannerImage)

        binding.bannerImage.setOnClickListener {
            redirect(model.link, resources.getString(R.string.redirect_via_banner_tap))
        }
    }

    private fun showBannerText(model: Banner) {
        binding.bannerText.visibility = View.VISIBLE
        binding.bannerText.text = model.title
        binding.bannerText.setOnClickListener {
            redirect(model.link, resources.getString(R.string.redirect_via_banner_tap))
        }
    }

    private fun redirect(link: String, content: String? = null) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        content?.let { intent.putExtra(EXTRA_CONTENT, it) }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun noBanner() {
        binding.bannerImage.reset()
        binding.bannerText.reset()
    }

    private fun View.reset() {
        visibility = View.GONE
        setOnClickListener(null)
    }

    private fun configureHelp(view: View) {
        view.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.help)
                .setItems(R.array.query_rule_custom_data_help, null)
                .setPositiveButton(R.string.ok, null)
                .show()
        }
    }
}
