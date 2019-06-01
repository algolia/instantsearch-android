package index

import com.algolia.instantsearch.helper.index.IndexPresenterImpl
import com.algolia.search.model.IndexName
import com.algolia.search.model.response.ResponseSearch
import mockClient
import shouldEqual
import kotlin.test.Test


class TestIndexPresenter {

    private val indexName = IndexName("name")
    private val client = mockClient(ResponseSearch(), ResponseSearch.serializer())
    private val index = client.initIndex(indexName)

    @Test
    fun impl() {
        IndexPresenterImpl(index) shouldEqual indexName.raw
    }
}