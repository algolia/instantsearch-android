package relatedItems

import com.algolia.instantsearch.core.Indexable
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    override val objectID: String,
    val name: String,
    val brand: String,
    val categories: List<String>,
) : Indexable

data class SimpleProduct(
    override val objectID: String,
    val brand: String
) : Indexable
