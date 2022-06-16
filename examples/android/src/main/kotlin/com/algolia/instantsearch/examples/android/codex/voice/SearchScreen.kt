package com.algolia.instantsearch.examples.android.codex.voice

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.algolia.instantsearch.compose.highlighting.toAnnotatedString
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.examples.android.showcase.compose.ui.component.SearchBox
import com.algolia.instantsearch.compose.searchbox.SearchBoxState

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchBoxState: SearchBoxState,
    productsState: HitsState<Product>,
    onMicClick: () -> Unit,
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
            trailingIcon = {
                if (searchBoxState.query.isEmpty()) {
                    Icon(
                        modifier = Modifier.clickable(onClick = onMicClick),
                        imageVector = Icons.Default.Mic,
                        contentDescription = null
                    )
                } else {
                    Icon(
                        modifier = Modifier.clickable { searchBoxState.setText("") },
                        imageVector = Icons.Default.Clear,
                        contentDescription = null
                    )
                }
            }
        )

        Products(products = productsState.hits)
    }
}

@Composable
private fun Products(
    modifier: Modifier = Modifier,
    products: List<Product>,
) {
    if (products.isEmpty()) return

    Column(modifier) {
        products.forEach { actor ->
            Product(product = actor)
        }
    }
}

@Composable
private fun Product(
    modifier: Modifier = Modifier,
    product: Product
) {
    Row(
        modifier
            .background(MaterialTheme.colors.surface)
            .padding(12.dp)
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
                text = product.highlightedName?.toAnnotatedString()
                    ?: AnnotatedString(product.name),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = product.highlightedDescription?.toAnnotatedString() ?: AnnotatedString(
                    product.description
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.caption,
            )
        }
    }
}
