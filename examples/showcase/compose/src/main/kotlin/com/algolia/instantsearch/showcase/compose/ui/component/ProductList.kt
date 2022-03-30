package com.algolia.instantsearch.showcase.compose.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.algolia.instantsearch.showcase.compose.model.Product

@Composable
fun ProductList(
    modifier: Modifier = Modifier,
    products: List<Product>,
    onItemClick: (Product) -> Unit = {}
) {
    LazyColumn(modifier) {
        items(products) { product ->
            Surface(elevation = 1.dp) {
                ProductItem(
                    product = product,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(product) }
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun ProductItem(modifier: Modifier = Modifier, product: Product) {
    Row(modifier) {
        Image(
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop,
            painter = rememberImagePainter(
                data = product.image,
                builder = {
                    placeholder(android.R.drawable.ic_menu_report_image)
                    error(android.R.drawable.ic_menu_report_image)
                },
            ),
            contentDescription = "product image",
        )
        Column(Modifier.padding(start = 16.dp)) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = product.brand ?: "",
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = product.type,
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
