package com.algolia.exchange.query.categories.hits

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.algolia.instantsearch.compose.highlighting.toAnnotatedString
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBox
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.highlighting.DefaultPostTag
import com.algolia.instantsearch.core.highlighting.DefaultPreTag
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.search.model.search.Facet

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchBoxState: SearchBoxState,
    hitsState: HitsState<Product>,
    categoriesState: HitsState<Facet>,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        SearchBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            searchBoxState = searchBoxState,
        )

        Categories(categories = categoriesState.hits)
        Products(products = hitsState.hits)
    }
}

@Composable
private fun Categories(
    modifier: Modifier = Modifier,
    categories: List<Facet>,
) {
    Column(modifier) {
        SectionTitle(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 4.dp),
            title = "Categories"
        )
        categories.forEach { category ->
            CategoryRow(category = category)
        }
    }
}

@Composable
private fun CategoryRow(
    modifier: Modifier = Modifier,
    category: Facet
) {
    Row(
        modifier
            .background(MaterialTheme.colors.surface)
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Category,
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
            contentDescription = null
        )
        Text(
            text = category.highlighted.toAnnotatedString(),
            modifier = Modifier.padding(start = 12.dp),
        )
    }
}


@Composable
private fun Products(products: List<Product>) {
    SectionTitle(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        title = "Products"
    )
    products.forEach { product ->
        ProductRow(product = product)
    }
}

@Composable
private fun ProductRow(modifier: Modifier = Modifier, product: Product) {
    Row(
        modifier
            .background(MaterialTheme.colors.surface)
            .padding(12.dp)
    ) {
        Image(
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop,
            painter = rememberImagePainter(
                data = product.image,
                builder = {
                    placeholder(R.drawable.ic_menu_report_image)
                    error(R.drawable.ic_menu_report_image)
                },
            ),
            contentDescription = product.name,
        )

        Column(Modifier.padding(start = 8.dp)) {
            Text(
                text = product.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = product.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.caption,
            )
        }
    }
}

@Composable
private fun SectionTitle(modifier: Modifier = Modifier, title: String) {
    Text(
        modifier = modifier,
        text = title, style = MaterialTheme.typography.subtitle2,
        color = MaterialTheme.colors.onBackground.copy(alpha = 0.4f),
    )
}

private fun String.toAnnotatedString(): AnnotatedString {
    return HighlightTokenizer(DefaultPreTag, DefaultPostTag)(this).toAnnotatedString()
}
