package index

import com.algolia.instantsearch.index.IndexPresenterImpl
import com.algolia.search.model.IndexName
import mockClient
import shouldEqual
import kotlin.test.Test

class TestIndexPresenter {

    private val indexName = IndexName("name")
    private val client = mockClient()
    private val index = client.initIndex(indexName)

    @Test
    fun impl() {
        IndexPresenterImpl(index) shouldEqual indexName.raw
    }
}
