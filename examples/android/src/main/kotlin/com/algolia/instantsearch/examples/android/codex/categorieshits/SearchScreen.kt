package com.algolia.instantsearch.examples.android.codex.categorieshits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.algolia.client.model.search.FacetHits
import com.algolia.instantsearch.compose.highlighting.toAnnotatedString
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.instantsearch.examples.android.AppColors
import com.algolia.instantsearch.examples.android.showcase.compose.ui.component.SearchBox

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchBoxState: SearchBoxState,
    categoriesState: HitsState<FacetHits>,
    productsState: HitsState<Product>,
) {
    Scaffold(modifier = modifier, topBar = {
        SearchBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            searchBoxState = searchBoxState,
        )
    }) { paddings ->
        LazyColumn(
            Modifier
                .padding(paddings)
                .fillMaxSize()
        ) {
            stickyHeader { SectionTitle(title = "Categories") }
            items(categoriesState.hits) { category -> CategoryItem(category = category) }

            stickyHeader { SectionTitle(title = "Products") }
            items(productsState.hits) { product -> ProductItem(product = product) }
        }
    }
}

@Composable
private fun CategoryItem(
    modifier: Modifier = Modifier, category: FacetHits
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Category,
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
            contentDescription = null
        )
        Text(
            text = category.highlightedString(),
            modifier = Modifier.padding(start = 12.dp),
        )
    }
}


@Composable
private fun ProductItem(
    modifier: Modifier = Modifier,
    product: Product,
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        AsyncImage(
            modifier = Modifier.size(64.dp),
            model = product.image,
            placeholder = painterResource(id = android.R.drawable.ic_menu_report_image),
            error = painterResource(id = android.R.drawable.ic_menu_report_image),
            contentScale = ContentScale.Crop,
            contentDescription = product.name,
        )

        Column(Modifier.padding(start = 8.dp)) {
            Text(
                text = product.highlightedName?.toAnnotatedString(spanStyle = SpanStyle(color = AppColors.nebulaBlue))
                    ?: AnnotatedString(product.name),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = product.highlightedDescription?.toAnnotatedString(spanStyle = SpanStyle(color = AppColors.nebulaBlue))
                    ?: AnnotatedString(product.description),
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
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        text = title, style = MaterialTheme.typography.subtitle2,
        color = MaterialTheme.colors.onBackground.copy(alpha = 0.4f),
    )
}

private fun FacetHits.highlightedString(): AnnotatedString = HighlightTokenizer()(highlighted ?: "").toAnnotatedString()
