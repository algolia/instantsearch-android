package com.algolia.instantsearch.showcase.compose.customdata

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.algolia.instantsearch.compose.customdata.QueryRuleCustomDataState
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.customdata.QueryRuleCustomDataConnector
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.SearchMode
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.compose.*
import com.algolia.instantsearch.showcase.compose.R
import com.algolia.instantsearch.showcase.compose.customdata.TemplateActivity.Companion.EXTRA_CONTENT
import com.algolia.instantsearch.showcase.compose.model.Banner
import com.algolia.instantsearch.showcase.compose.model.Product
import com.algolia.instantsearch.showcase.compose.ui.BlueDark
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.WhiteLight
import com.algolia.instantsearch.showcase.compose.ui.component.HelpDialog
import com.algolia.instantsearch.showcase.compose.ui.component.ProductList
import com.algolia.instantsearch.showcase.compose.ui.component.SearchTopBar
import com.algolia.search.helper.deserialize

class QueryRuleCustomDataShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val hitsState = HitsState<Product>()

    private val searchBoxState = SearchBoxState()
    private val searchBox = SearchBoxConnector(searcher, searchMode = SearchMode.AsYouType)

    private val queryRuleCustomDataState = QueryRuleCustomDataState<Banner>()
    private val queryRuleCustomData = QueryRuleCustomDataConnector<Banner>(
        searcher = searcher,
        presenter = queryRuleCustomDataState
    )

    private val connections = ConnectionHandler(searchBox, queryRuleCustomData)

    init {
        connections += searchBox.connectView(searchBoxState)
        connections += searcher.connectHitsView(hitsState) { it.hits.deserialize(Product.serializer()) }

        searchBox.viewModel.eventSubmit.subscribe {
            val model = queryRuleCustomData.viewModel.item.value ?: return@subscribe
            if (model.banner == null && model.title ==null) {
                redirect(model.link, resources.getString(R.string.redirect_via_submit))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                val content = stringResource(id = R.string.redirect_via_banner_tap)
                QueryRuleCustomDataScreen(
                    searchBoxState,
                    hitsState,
                    queryRuleCustomDataState
                ) { link ->
                    redirect(link, content)
                }
            }
        }
        configureSearcher(searcher)
        searcher.searchAsync()
    }

    @Composable
    fun QueryRuleCustomDataScreen(
        searchBoxState: SearchBoxState,
        hitsState: HitsState<Product>,
        queryRuleCustomDataState: QueryRuleCustomDataState<Banner>,
        onBannerClick: (String) -> Unit = {}
    ) {
        val openDialog = remember { mutableStateOf(false) }
        Scaffold(
            topBar = {
                SearchTopBar(
                    searchBoxState = searchBoxState,
                    onBackPressed = ::onBackPressed,
                    icon = Icons.Default.Info,
                    onIconClick = { openDialog.value = true }
                )
            },
            content = {
                Column {
                    Banner(queryRuleCustomDataState, onBannerClick)
                    ProductList(
                        modifier = Modifier.fillMaxWidth(),
                        products = hitsState.hits
                    )
                }
            }
        )
        HelpDialog(openDialog, stringArrayResource(R.array.query_rule_custom_data_help))
    }

    @Composable
    fun Banner(
        queryRuleCustomDataState: QueryRuleCustomDataState<Banner>,
        onClick: (String) -> Unit
    ) {
        queryRuleCustomDataState.item?.let { model ->
            when {
                model.banner != null -> BannerImage(model, onClick)
                model.title != null -> BannerText(model, onClick)
            }
        }
    }

    @Composable
    fun BannerImage(banner: Banner, onClick: (String) -> Unit = {}) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { onClick(banner.link) }),
            contentScale = ContentScale.FillWidth,
            painter = rememberImagePainter(data = banner.banner),
            contentDescription = "banner",
        )
    }

    @Composable
    fun BannerText(banner: Banner, onClick: (String) -> Unit = {}) {
        if (banner.title == null) return
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(BlueDark)
                .clickable(onClick = { onClick(banner.link) })
                .padding(8.dp),
            text = banner.title,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            color = WhiteLight,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    private fun redirect(link: String, content: String? = null) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        content?.let { intent.putExtra(EXTRA_CONTENT, it) }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connections.clear()
    }
}
