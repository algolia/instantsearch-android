package index

import com.algolia.instantsearch.index.DefaultIndexPresenter
import mockClient
import shouldEqual
import kotlin.test.Test

class TestIndexPresenter {

    private val indexName = "name"
    private val client = mockClient()

    @Test
    fun impl() {
        DefaultIndexPresenter(indexName) shouldEqual indexName
    }
}
