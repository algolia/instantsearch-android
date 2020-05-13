package relatedItems

import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.Serializable

@Serializable
internal data class Product(
    override val objectID: ObjectID,
    val name: String,
    val brand: String,
    val categories: List<String>
) : Indexable
